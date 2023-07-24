package online.devwiki.common.oauth.config;

import com.nimbusds.jose.JOSEException;
import online.devwiki.common.oauth.config.commonUser.CommonUserLoginAuthenticationProvider;
import online.devwiki.common.oauth.config.commonUser.CommonUserLoginAuthenticationSuccessHandler;
import online.devwiki.common.oauth.domain.service.CommonUserAuthenticationService;
import online.devwiki.common.oauth.domain.service.DevWikiUserDetailService;
import online.devwiki.common.oauth.federation.FederatedIdentityAuthenticationSuccessHandler;
import online.devwiki.common.oauth.jwt.JwtAuthenticationProvider;
import online.devwiki.common.oauth.jwt.JwtConfig;
import online.devwiki.common.oauth.jwt.JwtDecodeService;
import online.devwiki.common.oauth.jwt.JwtEncodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.List;

@EnableWebSecurity
@Configuration
public class DefaultSecurityConfig {

    @Autowired
    private DevWikiUserDetailService devWikiUserDetailService;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http
            , AuthenticationManager commonUserAuthenticationManager
            , AuthenticationSuccessHandler commonUserLoginAuthenticationSuccessHandler
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(commonUserAuthenticationManager)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/common/oauth/health",
                                        "/api/v1/auth/authenticate",
                                        "/api/v1/auth/refresh-token",
                                        "/api/v1/auth/.well-known/jwks.json",
                                        "/assets/**",
                                        "/webjars/**",
                                        "/login").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login")
                                .successHandler(commonUserLoginAuthenticationSuccessHandler)
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .loginPage("/login")
                                .successHandler(authenticationSuccessHandler())
                )
        ;

        return http.build();
    }

    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new FederatedIdentityAuthenticationSuccessHandler();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public AuthenticationManager commonUserAuthenticationManager() {
        return new ProviderManager(List.of(
                new CommonUserLoginAuthenticationProvider(devWikiUserDetailService),
                new JwtAuthenticationProvider())
        );
    }

    @Bean
    public AuthenticationSuccessHandler commonUserLoginAuthenticationSuccessHandler(CommonUserAuthenticationService commonUserAuthenticationService, DevWikiUserDetailService devWikiUserDetailService) {
        return new CommonUserLoginAuthenticationSuccessHandler(commonUserAuthenticationService, devWikiUserDetailService);
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public JwtEncodeService jwtEncodeService(JwtConfig jwtConfig) throws JOSEException {
        return new JwtEncodeService(jwtConfig);
    }

    @Bean
    public JwtDecodeService jwtDecodeService(JwtConfig jwtConfig) throws JOSEException {
        return new JwtDecodeService(jwtConfig);
    }
}

