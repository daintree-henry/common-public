package online.devwiki.common.oauth.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.IOUtils;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JwtConfig {

    private static final String PRIVATE_KEY_PATH = "jwk/common_oauth_private_key.pem";

    @Value("${application.security.jwt.key-id:}")
    private String keyId;

    public JwtEncoder jwtEncoder() throws JOSEException {
        JWKSource<SecurityContext> jwkPrivateKeySource = getJWKSource(PRIVATE_KEY_PATH);
        return new NimbusJwtEncoder(jwkPrivateKeySource);
    }

    public JwtDecoder jwtDecoder() throws JOSEException {
        JWKSource<SecurityContext> jwkPrivateKeySource = getJWKSource(PRIVATE_KEY_PATH);

        ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
        jwtProcessor.setJWSKeySelector(new JWSVerificationKeySelector<>(JWSAlgorithm.RS256, jwkPrivateKeySource));
        return new NimbusJwtDecoder(jwtProcessor);
    }

    public JWKSet getPublicJWKSet() throws JOSEException {
        JWKSource<SecurityContext> jwkSource = getJWKSource(PRIVATE_KEY_PATH);
        RSAKey rsaKey = (RSAKey) jwkSource.get(new JWKSelector(new JWKMatcher.Builder().keyID(keyId).build()), null).get(0);
        RSAKey publicRsaKey = rsaKey.toPublicJWK();
        return new JWKSet(publicRsaKey);
    }

    private JWKSource<SecurityContext> getJWKSource(String keyPath) throws JOSEException {
        ClassLoader classLoader = JwtConfig.class.getClassLoader();
        InputStream keyStream = classLoader.getResourceAsStream(keyPath);
        String keyContent = "";
        try {
            keyContent = IOUtils.readInputStreamToString(keyStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new JOSEException(e.getMessage());
        }

        JWK jwk = JWK.parseFromPEMEncodedObjects(keyContent);

        RSAKey rsaKey = (RSAKey) jwk;
        jwk = new RSAKey.Builder(rsaKey)
                .keyUse(rsaKey.getKeyUse())
                .algorithm(rsaKey.getAlgorithm())
                .keyID(keyId)
                .build();

        return new ImmutableJWKSet<>(new JWKSet(jwk));
    }
}
