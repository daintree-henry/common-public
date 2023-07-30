package online.devwiki.common.oauth.jwt;

import online.devwiki.common.user.role.CommonRoleDto;

import java.time.Instant;
import java.util.List;

public class JwtPayload {
    private String sub;
    private List<CommonRoleDto> roles;
    private String iss;
    private Instant exp;
    private Boolean accountVerified;
    private String status;
    private String name;
    private Long userId;
    private String loginId;

    public JwtPayload() {
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public List<CommonRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<CommonRoleDto> roles) {
        this.roles = roles;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public Instant getExp() {
        return exp;
    }

    public void setExp(Instant exp) {
        this.exp = exp;
    }

    public Boolean getAccountVerified() {
        return accountVerified;
    }

    public void setAccountVerified(Boolean accountVerified) {
        this.accountVerified = accountVerified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
