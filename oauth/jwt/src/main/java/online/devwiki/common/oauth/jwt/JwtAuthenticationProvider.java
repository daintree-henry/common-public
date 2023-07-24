package online.devwiki.common.oauth.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtTokenAuthentication jwtTokenAuth = (JwtTokenAuthentication) authentication;
        String username = jwtTokenAuth.getName();

        jwtTokenAuth.eraseCredentials();
        jwtTokenAuth.setAuthenticated(true);
        return jwtTokenAuth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtTokenAuthentication.class);
    }
}

