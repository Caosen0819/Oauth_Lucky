package cn.sen.lucky.auth.config;

import cn.sen.lucky.common.model.SecurityUser;
import cn.sen.lucky.common.model.TokenConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.LinkedHashMap;

/**
 * @Author caosen
 * @data 2022/10/7--13:15
 */
@Configuration
public class AccessTokenConfig {
    /**
     * JWT的秘钥
     * TODO 实际项目中需要统一配置到配置文件中，资源服务也需要用到
     */
    private final static String SIGN_KEY="myjszl";

    /**
     * 令牌的存储策略
     */
    @Bean
    public TokenStore tokenStore() {
        //使用JwtTokenStore生成JWT令牌
        System.out.println("生成令牌");
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * JWTTokenStore 他的token信息，是把认证信息加密后封装在JWT负载中，怎么加密？就是这个方法
     * JwtAccessTokenConverter 令牌增强类，用于JWT令牌和OAuth身份进行转换 这里配置它
     * TokenEnhancer的子类，在JWT编码的令牌值和OAuth身份验证信息之间进行转换。
     * TODO：后期可以使用非对称加密
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        System.out.println("令牌增强");
        JwtAccessTokenConverter converter = new JwtAccessTokenEnhancer();
        // 设置秘钥
        converter.setSigningKey(SIGN_KEY);

        converter.setAccessTokenConverter(new JwtEnhanceAccessTokenConverter());

        return converter;
    }

    /**
     * JWT令牌增强器，继承JwtAccessTokenConverter
     * 将业务所需的额外信息放入令牌中，这样下游微服务就能解析令牌获取
     */
    public static class JwtAccessTokenEnhancer extends JwtAccessTokenConverter {

//单纯的测试罢了，必须注视，不然难以继承JwtAccessTokenConverter,
//        JwtAccessTokenEnhancer(){
//            System.out.println("Jwt令牌增强");
//        }

        /**
         * 重写enhance方法，在其中扩展
         */
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            System.out.println("-----------进入到jwt令牌增强的enhance方法-------");
            System.out.println(authentication);
            System.out.println("-----------调取detailService---------");
            Object principal = authentication.getUserAuthentication().getPrincipal();
            System.out.println((SecurityUser)principal);


            if (principal instanceof SecurityUser){
                //获取userDetailService中查询到用户信息
                SecurityUser user=(SecurityUser)principal;
                //将额外的信息放入到LinkedHashMap中
                LinkedHashMap<String,Object> extendInformation=new LinkedHashMap<>();
                //设置用户的userId
                extendInformation.put(TokenConstant.USER_ID,user.getUserId());
                //添加到additionalInformation
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extendInformation);
            }
            return super.enhance(accessToken, authentication);
        }
    }
}