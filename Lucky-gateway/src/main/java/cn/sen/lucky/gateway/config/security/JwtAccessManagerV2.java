package cn.sen.lucky.gateway.config.security;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import cn.sen.lucky.common.constant.AuthConstant;
import cn.sen.lucky.common.domain.UserDto;
import cn.sen.lucky.common.model.SysConstant;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Caosen
 * 图中的第7步
 * 鉴权管理器 用于认证成功之后对用户的权限进行鉴权
 * 第二个版本，集成RBAC，实现动态权限校验
 * 前面文章请看 JwtAccessManager
 * @Date 2021/12/31
 */
@Slf4j
@Component
@Primary
public class JwtAccessManagerV2 implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {

        System.out.println("第七步***来到鉴权管理器");
//        //匹配url
//        AntPathMatcher antPathMatcher = new AntPathMatcher();
//        //从Redis中获取当前路径可访问角色列表
//        URI uri = authorizationContext.getExchange().getRequest().getURI();
//        //请求方法 POST,GET
//        String method = authorizationContext.getExchange().getRequest().getMethodValue();
//        System.out.println("调用的方法：" + method);
//        /**
//         * TODO 为了适配restful接口，比如 GET:/api/.... POST:/api/....  *:/api/.....  星号匹配所有
//         */
//        String restFulPath = method + SysConstant.METHOD_SUFFIX + uri.getPath();
//        System.out.println("完整路径：" + restFulPath);

//        //获取所有的uri->角色对应关系
//        Map<String, List<String>> entries = redisTemplate.opsForHash().entries(SysConstant.OAUTH_URLS);
//        System.out.println("从redis获取的所有的对应关系：如下\n"+entries);

//        //角色集合
//        List<String> authorities = new ArrayList<>();
//        entries.forEach((path, roles) -> {
//            //路径匹配则添加到角色集合中
//            if (antPathMatcher.match(path, restFulPath)) {
//                authorities.addAll(roles);
//                System.out.println("redis中的path：" + path + "\t"+ "请求的完整路径：" + restFulPath);
//                System.out.println("这两者相匹配，所以加入权限"+ roles);
//            }
//        });
//        System.out.println();
//        System.out.println("加入后的此次请求需要的的权限为："+authorities);
//        System.out.println("如果验证时请求用户具备这两个权限之一的权限，那么则通过");
        //认证通过且角色匹配的用户可访问当前路径
//        return mono
//                //判断是否认证成功
//                .filter(Authentication::isAuthenticated)
//                //获取认证后的全部权限
//                .flatMapIterable(Authentication::getAuthorities)
//                .map(GrantedAuthority::getAuthority)
//                //如果权限包含则判断为true
//                .any(authority->{
//                    //超级管理员直接放行
//                    if (StrUtil.equals(SysConstant.ROLE_ROOT_CODE,authority))
//                        return true;
//                    //其他必须要判断角色是否存在交集
//                    System.out.println(authority);
//                    return CollectionUtil.isNotEmpty(authorities) && authorities.contains(authority);
//                })
//                .map(AuthorizationDecision::new)
//                .defaultIfEmpty(new AuthorizationDecision(false));


        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        URI uri = request.getURI();
        System.out.println("url：" + uri);
        PathMatcher pathMatcher = new AntPathMatcher();
        //白名单路径直接放行
        List<String> ignoreUrls = ignoreUrlsConfig.getUrls();
        for (String ignoreUrl : ignoreUrls) {
            if (pathMatcher.match(ignoreUrl, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        System.out.println("非白名单");
        //对应跨域的预检请求直接放行
        if(request.getMethod()== HttpMethod.OPTIONS){
            System.out.println("预检查");
            return Mono.just(new AuthorizationDecision(true));
        }
        System.out.println("已过预检查");
        //不同用户体系登录不允许互相访问
        try {
            String token = request.getHeaders().getFirst(AuthConstant.JWT_TOKEN_HEADER);
            if(StrUtil.isEmpty(token)){
                return Mono.just(new AuthorizationDecision(false));
            }
            String realToken = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
            System.out.println("realtoken：" + realToken);
            JWSObject jwsObject = JWSObject.parse(realToken);
            String userStr = jwsObject.getPayload().toString();
            UserDto userDto = JSONUtil.toBean(userStr, UserDto.class);
            if (AuthConstant.ADMIN_CLIENT_ID.equals(userDto.getClientId()) && !pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(false));
            }
            if (AuthConstant.PORTAL_CLIENT_ID.equals(userDto.getClientId()) && pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
                return Mono.just(new AuthorizationDecision(false));
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return Mono.just(new AuthorizationDecision(false));
        }
        //非管理端路径直接放行
        if (!pathMatcher.match(AuthConstant.ADMIN_URL_PATTERN, uri.getPath())) {
            return Mono.just(new AuthorizationDecision(true));
        }
        System.out.println("非管理端");
        //管理端路径需校验权限
        Map<Object, Object> resourceRolesMap = redisTemplate.opsForHash().entries(AuthConstant.RESOURCE_ROLES_MAP_KEY);
        //System.out.println(resourceRolesMap);
        Iterator<Object> iterator = resourceRolesMap.keySet().iterator();
        List<String> authorities = new ArrayList<>();
        while (iterator.hasNext()) {
            String pattern = (String) iterator.next();
//            System.out.println(pattern);
//            System.out.println(uri.getPath());
            if (pathMatcher.match(pattern, uri.getPath())) {
//                System.out.println("111111111111");
                authorities.addAll(Convert.toList(String.class, resourceRolesMap.get(pattern)));
            }
        }
        authorities = authorities.stream().map(i -> i = AuthConstant.AUTHORITY_PREFIX + i).collect(Collectors.toList());
        List<String> authorities2 = authorities;
        System.out.println("得到权限" + authorities);
        //认证通过且角色匹配的用户可访问当前路径
        //测试的时候用了一个root权限
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authority->{
                    //超级管理员直接放行
                    if (StrUtil.equals(SysConstant.ROLE_ROOT_CODE,authority))
                        return true;
                    //其他必须要判断角色是否存在交集
                    System.out.println("判断");
                    return CollectionUtil.isNotEmpty(authorities2) && authorities2.contains(authority);
                })
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));

    }


}
