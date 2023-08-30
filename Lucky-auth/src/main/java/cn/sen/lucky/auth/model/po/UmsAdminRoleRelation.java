package cn.sen.lucky.auth.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author caosen
 * @Date 2022/10/17 10:15
 */

@Data
@Entity
@Table(name = "ums_admin_role_relation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmsAdminRoleRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "role_id")
    private Long roleId;

}
