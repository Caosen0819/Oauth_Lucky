package cn.sen.lucky.auth.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Author caosen
 * @Date 2022/10/17 10:23ums_role_permission_relation
 */
@Data
@Entity
@Table(name = "ums_role_permission_relation")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmsRolePermissonRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增主键
    private Long id;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "permission_id")
    private Long permissionId;
}
