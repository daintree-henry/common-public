package online.devwiki.common.oauth.jwt;

import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static online.devwiki.common.oauth.jwt.JwtDecodeService.ISSURE;

public class JwtEncodeService {

    private final JwtEncoder jwtEncoder;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public JwtEncodeService(JwtConfig jwtConfig) throws JOSEException {
        this.jwtEncoder = jwtConfig.jwtEncoder();
    }

    public Jwt generateStringToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public Jwt generateRefreshToken(
            UserDetails userDetails
    ) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private Jwt buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuer(ISSURE)
                .expiresAt(Instant.now().plusSeconds(expiration));

        for (Map.Entry<String, Object> entry : extraClaims.entrySet()) {
            claimsBuilder.claim(entry.getKey(), entry.getValue());
        }

        JwtClaimsSet claims = claimsBuilder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

}
