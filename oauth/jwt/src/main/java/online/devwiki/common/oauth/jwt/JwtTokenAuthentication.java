package online.devwiki.common.oauth.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtTokenAuthentication extends AbstractAuthenticationToken {

    private final JwtPayload jwtPayload;

    public JwtTokenAuthentication(JwtPayload jwtPayload) {
        super(jwtPayload.getRoles());
        this.jwtPayload = jwtPayload;
        setDetails(jwtPayload);
    }

    @Override
    public Object getCredentials() {
        return jwtPayload;
    }

    @Override
    public Object getPrincipal() {
        return jwtPayload.getSub();
    }

}
