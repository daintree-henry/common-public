package online.devwiki.sampleresourceserver.config;

import com.nimbusds.jose.JOSEException;
import online.devwiki.common.oauth.filter.CommonUserDetailService;
import online.devwiki.common.oauth.filter.JwtAuthenticationTokenFilter;
import online.devwiki.common.oauth.jwt.JwtAuthenticationProvider;
import online.devwiki.common.oauth.jwt.JwtConfig;
import online.devwiki.common.oauth.jwt.JwtDecodeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import java.util.List;

@Configuration
public class CommonSecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http
            , AuthenticationManager authenticationManager
            , JwtDecodeService jwtDecodeService
    ) throws Exception {
        JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter(jwtDecodeService, authenticationManager);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/healthz").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterAfter(jwtAuthenticationTokenFilter, LogoutFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public JwtDecodeService jwtDecodeService(JwtConfig jwtConfig) throws JOSEException {
        return new JwtDecodeService(jwtConfig);
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(JwtDecodeService jwtDecodeService,
                                                                     AuthenticationManager authenticationManager) {
        return new JwtAuthenticationTokenFilter(jwtDecodeService, authenticationManager);
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
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(
                new JwtAuthenticationProvider())
        );
    }

    @Bean
    public UserDetailsService commonUserDetailService() {
        return new CommonUserDetailService();
    }
}
