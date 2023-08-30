package cn.sen.lucky.auth.sms.service.impl;

import cn.hutool.core.util.ArrayUtil;

import cn.sen.lucky.auth.dao.SysRoleRepository;
import cn.sen.lucky.auth.dao.SysUserRepository;
import cn.sen.lucky.auth.dao.SysUserRoleRepository;
import cn.sen.lucky.auth.model.po.UmsAdmin;
import cn.sen.lucky.auth.model.po.UmsAdminRoleRelation;
import cn.sen.lucky.auth.sms.service.SmsCodeUserDetailService;
import cn.sen.lucky.common.model.SecurityUser;
import cn.sen.lucky.common.model.SysConstant;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author caosen
 * @data 2022/10/10--14:18
 */
@Service
public class SmsCodeUserDetailServiceImpl implements SmsCodeUserDetailService {

    @Resource
    private SysUserRepository sysUserRepository;

    @Resource
    private SysUserRoleRepository sysUserRoleRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        UmsAdmin user = sysUserRepository.findByMobileAndStatus(mobile, 1);
        System.out.println("成功通过mobile找到user，user信息为：" + user);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("没有这个mobile" + mobile + "的用户");
        }

        List<UmsAdminRoleRelation> sysUserRoles = sysUserRoleRepository.findByAdminId(user.getId());

        List<String> roles = new ArrayList<>();
        for (UmsAdminRoleRelation sysUserRole : sysUserRoles) {
            sysRoleRepository.findById(sysUserRole.getRoleId()).ifPresent(o -> roles.add(SysConstant.ROLE_PREFIX + o.getCode()));
        }
        return SecurityUser.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(ArrayUtil.toArray(roles, String.class)))
                .build();

    }
}
