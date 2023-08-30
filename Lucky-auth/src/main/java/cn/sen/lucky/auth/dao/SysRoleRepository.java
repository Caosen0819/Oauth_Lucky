package cn.sen.lucky.auth.dao;

import cn.sen.lucky.auth.model.po.UmsRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepository  extends JpaRepository<UmsRole,Long> {
}
