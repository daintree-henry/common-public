package online.devwiki.common.oauth.config.commonUser;

import online.devwiki.common.oauth.domain.service.DevWikiUserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public record CommonUserLoginAuthenticationProvider(
        DevWikiUserDetailService devWikiUserDetailService) implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (devWikiUserDetailService.authenticateUser((String) authentication.getPrincipal(), (String) authentication.getCredentials())) {
            return new UsernamePasswordAuthenticationToken((String) authentication.getPrincipal(), (String) authentication.getCredentials(), null);
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}