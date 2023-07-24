package online.devwiki.common.oauth.jwt;

import java.io.Serializable;

public class JwtTokenDto implements Serializable {

    public String loginId;
    public String token;

    public JwtTokenDto(String loginId, String token) {
        this.loginId = loginId;
        this.token = token;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
