package online.devwiki.common.oauth.jwt;

import java.io.Serializable;

public class JwtTokenDto implements Serializable {

    public String userId;
    public String token;

    public JwtTokenDto(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
