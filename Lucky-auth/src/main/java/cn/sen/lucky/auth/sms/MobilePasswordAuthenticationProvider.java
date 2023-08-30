package cn.sen.lucky.auth.sms;

import cn.sen.lucky.auth.sms.service.SmsCodeUserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author caosen
 * @data 2022/10/10--15:22
 */
public class MobilePasswordAuthenticationProvider implements AuthenticationProvider {

    private final SmsCodeUserDetailService userDetailService;

    private final PasswordEncoder passwordEncoder;

    public MobilePasswordAuthenticationProvider(SmsCodeUserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobilePasswordAuthenticationToken authenticationToken = (MobilePasswordAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String password =(String) authenticationToken.getCredentials();
        //查询数据库
        System.out.println("进入到authenticate");
        System.out.println("3----调用userdetailservice方法");
        UserDetails user = userDetailService.loadUserByMobile(mobile);
        if (user == null) {
            throw new InternalAuthenticationServiceException("手机号验证的账号错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("手机号验证的密码错误");
        }

        MobilePasswordAuthenticationToken mobilePasswordAuthenticationToken = new MobilePasswordAuthenticationToken(user, password, user.getAuthorities());
        mobilePasswordAuthenticationToken.setDetails(mobilePasswordAuthenticationToken.getDetails());
        return mobilePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobilePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
