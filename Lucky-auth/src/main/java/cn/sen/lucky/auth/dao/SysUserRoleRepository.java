package cn.sen.lucky.auth.dao;

import cn.sen.lucky.auth.model.po.UmsAdminRoleRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepository extends JpaRepository<UmsAdminRoleRelation,Long> {
    List<UmsAdminRoleRelation> findByAdminId(Long adminId);
}
