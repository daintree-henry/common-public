package online.devwiki.common.oauth.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import online.devwiki.common.user.role.CommonPermissionDto;
import online.devwiki.common.user.role.CommonRoleDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.*;

import static online.devwiki.common.oauth.jwt.JwtConstant.*;

public class JwtDecodeService {

    private final JwtDecoder jwtDecoder;

    public JwtDecodeService(JwtConfig jwtConfig) throws JOSEException {
        this.jwtDecoder = jwtConfig.jwtDecoder();
    }

    public String extractUsername(String token) {
        return (String) extractClaim(token, CLAIM_USER_IDENTIFIER);
    }

    public Object extractClaim(String token, String key) {
        Map<String, Object> claims = extractAllClaims(token);
        return claims.getOrDefault(key, null);
    }

    public JwtPayload extractPayload(String token) {
        Map<String, Object> claims = extractAllClaims(token);
        JwtPayload jwtPayload = new JwtPayload();

        if (claims.containsKey(CLAIM_USER_IDENTIFIER))
            jwtPayload.setSub((String) claims.get(CLAIM_USER_IDENTIFIER));
        if (claims.containsKey(CLAIM_ROLES))
            jwtPayload.setRoles(parseRole((List<Object>) claims.get(CLAIM_ROLES)));
        if (claims.containsKey(CLAIM_ISSURER))
            jwtPayload.setIss((String) claims.get(CLAIM_ISSURER));
        if (claims.containsKey(CLAIM_EXPIRED))
            jwtPayload.setExp((Instant) claims.get(CLAIM_EXPIRED));
        if (claims.containsKey(CLAIM_VARIFIED))
            jwtPayload.setAccountVerified((Boolean) claims.get(CLAIM_VARIFIED));
        if (claims.containsKey(CLAIM_STATUS))
            jwtPayload.setStatus((String) claims.get(CLAIM_STATUS));
        if (claims.containsKey(CLAIM_USERNAME))
            jwtPayload.setName((String) claims.get(CLAIM_USERNAME));
        if (claims.containsKey(CLAIM_USERID))
            jwtPayload.setUserId((Long) claims.get(CLAIM_USERID));
        if (claims.containsKey(CLAIM_LOGINID))
            jwtPayload.setLoginId((String) claims.get(CLAIM_LOGINID));
        return jwtPayload;
    }

    public boolean isTokenJwt(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (!isTokenJwt(token)) return false;
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Instant expired = extractExpiration(token);
        Instant now = Instant.now();
        return now.isAfter(expired);
    }

    private Instant extractExpiration(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getExpiresAt();
    }

    private Map<String, Object> extractAllClaims(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaims();
    }

    private List<CommonRoleDto> parseRole(List<Object> objList) {
        List<CommonRoleDto> result = new ArrayList<>();

        for (Object object : objList) {
            LinkedTreeMap map = (LinkedTreeMap) object;

            Long roleId = (Long) map.get(CLAIM_ROLEID);
            String name = (String) map.get(CLAIM_ROLENAME);

            ArrayList<LinkedTreeMap> permissions = (ArrayList<LinkedTreeMap>) map.get(CLAIM_PERMISSIONS);
            Set<CommonPermissionDto> permissionSet = new HashSet<>();
            for (LinkedTreeMap permissionMap : permissions) {
                Long permissionId = (Long) permissionMap.get(CLAIM_PERMISSIONID);
                String permissionName = (String) permissionMap.get(CLAIM_PERMISSIONNAME);
                permissionSet.add(new CommonPermissionDto(permissionId, permissionName));
            }

            result.add(new CommonRoleDto(roleId, name, permissionSet));
        }

        return result;
    }

}
