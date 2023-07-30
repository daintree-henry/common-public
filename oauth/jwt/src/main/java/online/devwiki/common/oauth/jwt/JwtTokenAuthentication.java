package online.devwiki.common.oauth.jwt;

import online.devwiki.common.user.dto.CommonUserDetail;
import online.devwiki.common.user.dto.Status;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Set;

public class JwtTokenAuthentication extends AbstractAuthenticationToken {

    private final JwtPayload jwtPayload;

    public JwtTokenAuthentication(JwtPayload jwtPayload) {
        super(jwtPayload.getRoles());
        this.jwtPayload = jwtPayload;
        setDetails(CommonUserDetail.builder()
                .userId(jwtPayload.getUserId())
                .loginId(jwtPayload.getLoginId())
                .accountVerified(jwtPayload.getAccountVerified())
                .roleDtoSet(Set.copyOf(jwtPayload.getRoles()))
                .status(Status.valueOf(jwtPayload.getStatus()))
                .name(jwtPayload.getName())
                .build()
        );
    }

    @Override
    public Object getCredentials() {
        return jwtPayload;
    }

    @Override
    public Object getPrincipal() {
        return jwtPayload.getUserId();
    }
}
