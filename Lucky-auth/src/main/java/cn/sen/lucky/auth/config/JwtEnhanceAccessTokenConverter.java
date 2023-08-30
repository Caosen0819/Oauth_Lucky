package cn.sen.lucky.auth.config;

import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

/**
 * @author caosen
 * 令牌转换器，将身份认证信息转换后存储在令牌内
 */
public class JwtEnhanceAccessTokenConverter extends DefaultAccessTokenConverter {
    public JwtEnhanceAccessTokenConverter() {
        super.setUserTokenConverter(new JwtEnhanceUserAuthenticationConverter());
    }
}

