package cn.sen.lucky.auth.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author caosen
 * @data 2022/10/10--15:00
 */
public class MobilePwdGranter extends AbstractTokenGranter {

    public static final String GRANT_TYPE = "mobile_pwd";

    private final AuthenticationManager authenticationManager;
    public MobilePwdGranter(AuthenticationManager authenticationManager, AuthorizationServerTokenServices tokenServices
            , ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.authenticationManager = authenticationManager;
    }


    @Override
    public OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
        String mobile = parameters.get("mobile");
        String password = parameters.get("password");
        //将其中的密码移除
        parameters.remove("password");
        //自定义的token类
        Authentication userAuth = new MobilePasswordAuthenticationToken(mobile, password);

        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        System.out.println("1----进入MobilePwdGranter的getOauthAuthentication");
        System.out.println("自定义的userAuth:" + userAuth);
        //调用AuthenticationManager进行认证，内部会根据MobileAuthenticationToken找到对应的Provider进行认证
        System.out.println("2----调用MobilePasswordAuthenticationProvider里面的authenticate方法");
        userAuth = authenticationManager.authenticate(userAuth);
        System.out.println("4----得到自定义验证后的userAuth:" + userAuth);
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException("Could not authenticate mobile: " + mobile);
        }
        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

}
