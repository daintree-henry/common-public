package online.devwiki.common.oauth.config.commonUser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.devwiki.common.oauth.application.dto.AuthDto;
import online.devwiki.common.oauth.domain.service.CommonUserAuthenticationService;
import online.devwiki.common.oauth.domain.service.DevWikiUserDetailService;
import online.devwiki.common.user.dto.CommonUserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public record CommonUserLoginAuthenticationSuccessHandler(
        CommonUserAuthenticationService commonUserAuthenticationService
        , DevWikiUserDetailService userDetailService) implements AuthenticationSuccessHandler {

    public CommonUserLoginAuthenticationSuccessHandler(CommonUserAuthenticationService commonUserAuthenticationService, DevWikiUserDetailService userDetailService) {
        this.commonUserAuthenticationService = commonUserAuthenticationService;
        this.userDetailService = userDetailService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        getAccessTokenAndRedirect(response, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        getAccessTokenAndRedirect(response, authentication);
    }

    private void getAccessTokenAndRedirect(HttpServletResponse response, Authentication authentication) throws IOException {
        CommonUserDetail userDetail = userDetailService.loadUserByUsername((String) authentication.getPrincipal());
        AuthDto.AuthenticationResponse authenticationResponse = commonUserAuthenticationService.getAccessTokenByUserDetail(userDetail);
        Cookie accessToken = new Cookie("accessToken", authenticationResponse.getAccessToken());
        Cookie refreshToekn = new Cookie("refreshToekn", authenticationResponse.getRefreshToken());
        response.addCookie(accessToken);
        response.addCookie(refreshToekn);
        response.sendRedirect("/");
    }
}
