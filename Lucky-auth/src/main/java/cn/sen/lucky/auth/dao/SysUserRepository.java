package cn.sen.lucky.auth.dao;

import cn.sen.lucky.auth.model.po.UmsAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author csen
 */
@Repository
public interface SysUserRepository extends JpaRepository<UmsAdmin,Long> {
    UmsAdmin findByUsernameAndStatus(String username,Integer status);

    UmsAdmin findByMobileAndStatus(String mobile,Integer status);

}
