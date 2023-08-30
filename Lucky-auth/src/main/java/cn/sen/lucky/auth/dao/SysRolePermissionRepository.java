package cn.sen.lucky.auth.dao;

import cn.sen.lucky.auth.model.po.UmsRolePermissonRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SysRolePermissionRepository  extends JpaRepository<UmsRolePermissonRelation,Long> {
    List<UmsRolePermissonRelation> findByPermissionId(Long permissionId);
}
