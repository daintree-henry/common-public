package online.devwiki.sampleresourceserver.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.devwiki.common.oauth.jwt.JwtDecodeService;
import online.devwiki.common.oauth.jwt.JwtPayload;
import online.devwiki.common.oauth.jwt.JwtTokenAuthentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final AuthenticationManager authenticationManager;
    private final JwtDecodeService jwtDecodeService;

    public JwtAuthenticationTokenFilter(JwtDecodeService jwtDecodeService, AuthenticationManager authenticationManager) {
        this.jwtDecodeService = jwtDecodeService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            String jwtToken = header.substring(BEARER_PREFIX.length());
            JwtPayload payload = jwtDecodeService.extractPayload(jwtToken);

            JwtTokenAuthentication authentication = new JwtTokenAuthentication(payload);
            Authentication auth = authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
