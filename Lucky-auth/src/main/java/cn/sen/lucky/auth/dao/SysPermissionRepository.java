package cn.sen.lucky.auth.dao;

import cn.sen.lucky.auth.model.po.UmsPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysPermissionRepository extends JpaRepository<UmsPermission,Long> {
}
