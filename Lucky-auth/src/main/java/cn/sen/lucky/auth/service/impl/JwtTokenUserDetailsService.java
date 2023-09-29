package cn.sen.lucky.auth.service.impl;

import cn.hutool.core.util.ArrayUtil;

import cn.sen.lucky.auth.dao.SysRoleRepository;
import cn.sen.lucky.auth.dao.SysUserRepository;
import cn.sen.lucky.auth.dao.SysUserRoleRepository;
import cn.sen.lucky.auth.model.po.UmsAdmin;
import cn.sen.lucky.auth.model.po.UmsAdminRoleRelation;
import cn.sen.lucky.common.model.SecurityUser;
import cn.sen.lucky.common.model.SysConstant;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 这里用到的知识和springsecurity有关，UserDetailsService是springsecurity其中一个重要的组件，我们在增强token的时候就需要用到这个组件
 * @Author caosen
 * @data 2022/10/8--17:15
 */
@Service("JwtTokenUserDetailsService")
public class JwtTokenUserDetailsService implements UserDetailsService {


    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("1、先找用户");
        UmsAdmin user = sysUserRepository.findByUsernameAndStatus(username, 1);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在");
        }
        System.out.println(user);
        System.out.println("2、然后去找这个用户所对应的角色");
        List<UmsAdminRoleRelation> sysUserRoles = sysUserRoleRepository.findByAdminId(user.getId());
        System.out.println("用户不为空，角色：" + sysUserRoles);

        List<String> roles=new ArrayList<>();

        System.out.println("3、找到角色之后，去找这些角色对应的权限，那么这样就这个用户对应的权限也就找到了");
        for (UmsAdminRoleRelation sysUserRole : sysUserRoles) {
            sysRoleRepository.findById(sysUserRole.getRoleId()).ifPresent(o-> roles.add(SysConstant.ROLE_PREFIX+o.getCode()));
        }

        return SecurityUser.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                //将角色放入authorities中
                .authorities(AuthorityUtils.createAuthorityList(ArrayUtil.toArray(roles,String.class)))
                .build();

    }
}
