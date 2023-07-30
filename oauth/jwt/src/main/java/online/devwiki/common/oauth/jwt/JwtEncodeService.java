package online.devwiki.common.oauth.jwt;

import com.nimbusds.jose.JOSEException;
import online.devwiki.common.user.dto.CommonUserDetail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static online.devwiki.common.oauth.jwt.JwtConstant.*;

public class JwtEncodeService {
    public final static String ISSURER = "devwiki.online";
    private final JwtEncoder jwtEncoder;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public JwtEncodeService(JwtConfig jwtConfig) throws JOSEException {
        this.jwtEncoder = jwtConfig.jwtEncoder();
    }

    public Jwt generateStringToken(
            CommonUserDetail userDetails
    ) {
        return buildToken(this.toExtraClaim(userDetails), userDetails, jwtExpiration);
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
                .issuer(ISSURER)
                .expiresAt(Instant.now().plusSeconds(expiration));

        for (Map.Entry<String, Object> entry : extraClaims.entrySet()) {
            claimsBuilder.claim(entry.getKey(), entry.getValue());
        }

        JwtClaimsSet claims = claimsBuilder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    public Map<String, Object> toExtraClaim(CommonUserDetail detail) {
        Map<String, Object> extraClaim = new HashMap<>();
        if (detail.getRoleDtoSet() != null) extraClaim.put(CLAIM_ROLES, detail.getRoleDtoSet());
        if (detail.getStatus() != null)
            extraClaim.put(CLAIM_STATUS, detail.getStatus().toString());
        if (detail.getName() != null) extraClaim.put(CLAIM_USERNAME, detail.getName());
        if (detail.getUserId() != null) extraClaim.put(CLAIM_USERID, detail.getUserId());
        if (detail.getAccountVerified() != null)
            extraClaim.put(CLAIM_VARIFIED, detail.getAccountVerified());
        return extraClaim;
    }
}
