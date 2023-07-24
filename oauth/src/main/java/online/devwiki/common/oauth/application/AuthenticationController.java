package online.devwiki.common.oauth.application;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.devwiki.common.oauth.application.dto.AuthDto;
import online.devwiki.common.oauth.domain.service.CommonUserAuthenticationService;
import online.devwiki.common.oauth.jwt.JwtConfig;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final CommonUserAuthenticationService commonUserAuthenticationService;
    private final JwtConfig jwtConfig;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthDto.AuthenticationResponse> authenticateByLoginIdAndPassword(
            @Valid @RequestBody AuthDto.AuthenticationRequest request
    ) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(commonUserAuthenticationService.authenticateAndGetAuthResponse(request.getLoginId(), request.getPassword()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Boolean> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (commonUserAuthenticationService.refreshToken(request, response)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/is-valid-token")
    public ResponseEntity<Boolean> isValidToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (commonUserAuthenticationService.isValidToken(request)) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @GetMapping(value = "/.well-known/jwks.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getJwks() throws JOSEException {
        JWKSet jwkSet = jwtConfig.getPublicJWKSet();
        return jwkSet.toJSONObject();
    }
}
