package cn.sen.lucky.auth.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.Collection;

/**
 * @Author caosen
 * @data 2022/10/10--14:52
 */
public class MobilePasswordAuthenticationToken extends AbstractAuthenticationToken {


    private final Object principal;

    private Object credentials;

    public static final Long serialVersionId = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public MobilePasswordAuthenticationToken(Object principal, Object credentials,
                                             Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public MobilePasswordAuthenticationToken(Object mobile, Object password) {
        super(null);
        this.principal = mobile;
        this.credentials = password;
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
