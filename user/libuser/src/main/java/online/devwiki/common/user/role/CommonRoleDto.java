package online.devwiki.common.user.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonRoleDto implements GrantedAuthority {

    private Long roleId;
    private String roleName;
    private Set<CommonPermissionDto> permissionSet;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
