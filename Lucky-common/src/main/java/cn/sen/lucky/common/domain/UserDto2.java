package cn.sen.lucky.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author caosen
 * @Date 2022/10/18 16:14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UserDto2 {
    private Long user_id;
    private String user_name;
    private String password;
    private String exp;
    private String jti;

}