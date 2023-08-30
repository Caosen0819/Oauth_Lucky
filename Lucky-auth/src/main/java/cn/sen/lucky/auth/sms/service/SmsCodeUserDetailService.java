package cn.sen.lucky.auth.sms.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @Author caosen
 * @data 2022/10/10--14:16
 * 自定义的userdetailservice
 */
public interface SmsCodeUserDetailService {
    /**
     *根据手机号查询用户信息
     * @param mobile 手机号
     */
    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;
}
