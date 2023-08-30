package cn.sen.lucky.auth.service.impl;


import cn.sen.lucky.auth.dao.SysPermissionRepository;
import cn.sen.lucky.auth.dao.SysRolePermissionRepository;
import cn.sen.lucky.auth.dao.SysRoleRepository;
import cn.sen.lucky.auth.model.po.UmsPermission;
import cn.sen.lucky.auth.model.po.UmsRole;
import cn.sen.lucky.auth.model.po.UmsRolePermissonRelation;
import cn.sen.lucky.auth.model.vo.SysRolePermissionVO;
import cn.sen.lucky.auth.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author caosen
 * @data 2022/10/8--17:48
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private SysRolePermissionRepository sysRolePermissionRepository;

    @Resource
    private SysPermissionRepository sysPermissionRepository;

    @Resource
    private SysRoleRepository sysRoleRepository;


    @Override
    public List<SysRolePermissionVO> listRolePermission() {
        List<SysRolePermissionVO> sysRolePermissionVOS = new ArrayList<>();
        List<UmsPermission> allPermission = sysPermissionRepository.findAll();
        System.out.println("找出每一个permission对应的权限，然后对每个对应关系的进行遍历");
        for (UmsPermission sysPermission : allPermission) {
            List<UmsRolePermissonRelation> byPermissionId = sysRolePermissionRepository.findByPermissionId(sysPermission.getId());
            System.out.println("需要这么几个角色的权限id：" + byPermissionId);
            List<UmsRole> collect = byPermissionId.stream().map(k -> sysRoleRepository.findById(k.getRoleId()).get()).collect(Collectors.toList());
            System.out.println("现在拿到权限了，也就说这次循环我们赌赢的permission的权限是： " + collect);
            SysRolePermissionVO build = SysRolePermissionVO.builder()
                    .permissionId(sysPermission.getId())
                    .url(sysPermission.getUri())
                    .permissionName(sysPermission.getName())
                    .roles(collect)
                    .build();

            sysRolePermissionVOS.add(build);
        }


        System.out.println("到此为止，所有permission对应需要的权限名称都已经搞定：" + sysRolePermissionVOS);
        return sysRolePermissionVOS;
    }
}
