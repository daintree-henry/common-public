package online.devwiki.common.oauth.filter;

import online.devwiki.common.oauth.jwt.JwtTokenAuthentication;
import online.devwiki.common.user.dto.CommonUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CommonUserDetailService implements UserDetailsService {

    @Override
    public CommonUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtTokenAuthentication &&
                authentication.getPrincipal().toString().equals(username)) {
            return (CommonUserDetail) authentication.getDetails();
        }
        return null;
    }
}
