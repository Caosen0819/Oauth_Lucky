package cn.sen.lucky.auth.service;


import cn.sen.lucky.auth.model.vo.SysRolePermissionVO;

import java.util.List;

/**
 * @Author caosen
 * @data 2022/10/8--17:47
 */
public interface PermissionService {

    public List<SysRolePermissionVO> listRolePermission();
}
