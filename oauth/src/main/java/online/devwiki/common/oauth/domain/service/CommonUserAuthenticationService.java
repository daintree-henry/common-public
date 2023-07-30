package online.devwiki.common.oauth.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.devwiki.common.oauth.application.dto.AuthDto;
import online.devwiki.common.oauth.jwt.*;
import online.devwiki.common.user.dto.CommonUserDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonUserAuthenticationService {
    private final TokenService tokenService;
    private final DevWikiUserDetailService userDetailService;
    private final JwtEncodeService jwtEncodeService;
    private final JwtDecodeService jwtDecodeService;
    private final AuthenticationManager authenticationManager;

    public AuthDto.AuthenticationResponse authenticateAndGetAuthResponse(String loginId, String password) {
        if (isValidUser(loginId, password))
            return getAuthenticationResponse(userDetailService.loadUserByUsername(loginId));
        return null;
    }

    public CommonUserDetail authenticateAndGetUserDetail(String loginId, String password) {
        if (isValidUser(loginId, password))
            return userDetailService.loadUserByUsername(loginId);
        return null;
    }

    public AuthDto.AuthenticationResponse getAccessTokenByUserDetail(CommonUserDetail userDetail) {
        return getAuthenticationResponse(userDetail);
    }

    private AuthDto.AuthenticationResponse getAuthenticationResponse(CommonUserDetail userDetail) {
        Jwt accessToken = jwtEncodeService.generateStringToken(userDetail);
        Jwt refreshToken = jwtEncodeService.generateRefreshToken(userDetail);
        JwtPayload jwtTokenPayload = jwtDecodeService.extractPayload(accessToken.getTokenValue());
        authenticationManager.authenticate(new JwtTokenAuthentication(jwtTokenPayload));

        saveUserToken(userDetail, accessToken);
        return AuthDto.AuthenticationResponse.builder()
                .accessToken(accessToken.getTokenValue())
                .refreshToken(refreshToken.getTokenValue())
                .build();
    }

    public boolean refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String loginId;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return false;
        refreshToken = authHeader.substring(7);
        loginId = jwtDecodeService.extractUsername(refreshToken);
        CommonUserDetail userDetail = userDetailService.loadUserByUsername(loginId);
        if(userDetail == null) return false;

        try {
            if (jwtDecodeService.isTokenValid(refreshToken, userDetail)) {
                var accessToken = jwtEncodeService.generateStringToken(userDetail);
                saveUserToken(userDetail, accessToken);
                var authResponse = AuthDto.AuthenticationResponse.builder()
                        .accessToken(accessToken.getTokenValue())
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                return true;
            } else {
                log.error("토큰이 유효하지 않습니다. user: {}, refreshToken: {}", userDetail, refreshToken);
                return false;
            }
        }catch (Exception e){
            log.error("토큰 파싱 시 에러가 발생했습니다. error: {}", e.getMessage());
            return false;
        }
    }

    public boolean isValidToken(
            HttpServletRequest request
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String accessToken;
        final String loginId;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return false;
        accessToken = authHeader.substring(7);
        loginId = jwtDecodeService.extractUsername(accessToken);
        JwtTokenDto tokenDto = tokenService.findToken(loginId);

        return (Objects.equals(tokenDto.getToken(), accessToken));
    }

    private Boolean isValidUser(String loginId, String password) {
        return userDetailService.authenticateUser(loginId, password);
    }

    private void saveUserToken(UserDetails user, Jwt jwtToken) {
        JwtTokenDto token = new JwtTokenDto(user.getUsername(), jwtToken.getTokenValue());
        tokenService.saveToken(token);
    }
}
