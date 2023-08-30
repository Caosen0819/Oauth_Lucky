package cn.sen.lucky.auth.service.impl;

import cn.hutool.core.collection.CollectionUtil;

import cn.sen.lucky.auth.model.po.UmsRole;
import cn.sen.lucky.auth.model.vo.SysRolePermissionVO;
import cn.sen.lucky.auth.service.PermissionService;
import cn.sen.lucky.common.model.SysConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author caosen
 * @data 2022/10/8--17:44
 */

@Service
public class LoadRolePermissionService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private PermissionService permissionService;

    @PostConstruct
    public void init() {
        List<SysRolePermissionVO> sysRolePermissionVOS = permissionService.listRolePermission();
        System.out.println("获取所有的权限对应关系:");
        System.out.println(sysRolePermissionVOS);

        sysRolePermissionVOS.parallelStream().peek(k -> {
            List<String> roles=new ArrayList<>();
            if (CollectionUtil.isNotEmpty(k.getRoles())){
                System.out.println("k.getroles:" +k.getRoles());
                for (UmsRole role : k.getRoles()) {
                    roles.add(SysConstant.ROLE_PREFIX+role.getCode());
                }
            }
            //放入Redis中
            System.out.println("从数据库所有的url和权限的关系为：\n" + k.getRoles() + "\n"  + roles);
            redisTemplate.opsForHash().put(SysConstant.OAUTH_URLS,k.getUrl(), roles);
            System.out.println("--------------简化一下，放入redis------");
        }).collect(Collectors.toList());
    }
}
 //9 6 3 12 11 14 13 5 8 2 17 4 1 16