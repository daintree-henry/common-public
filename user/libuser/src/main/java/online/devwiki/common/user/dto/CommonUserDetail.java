package online.devwiki.common.user.dto;

import lombok.*;
import online.devwiki.common.user.role.CommonRoleDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonUserDetail implements UserDetails {

    Long userId;
    String loginId;
    String password;
    String email;
    LocalDate dateOfBirth;
    Status status;
    Gender gender;
    String name;
    Boolean accountVerified;
    LocalDateTime updatedAt;
    LocalDateTime createdAt;
    Set<CommonRoleDto> roleDtoSet;
    CommonUserInfoDto.General userInfo;

    public static CommonUserDetail toUserDetail(CommonUserDto.General generalDto) {
        return CommonUserDetail.builder()
                .userId(generalDto.getUserId())
                .loginId(generalDto.getLoginId())
                .email(generalDto.getEmail())
                .dateOfBirth(generalDto.getDateOfBirth())
                .status(generalDto.getStatus())
                .gender(generalDto.getGender())
                .name(generalDto.getName())
                .accountVerified(generalDto.getAccountVerified())
                .updatedAt(generalDto.getUpdatedAt())
                .createdAt(generalDto.getCreatedAt())
                .userInfo(generalDto.getUserInfo())
                .roleDtoSet(generalDto.getRoleSet())
                .build();
    }

    public Boolean hasAnyRole(String... roleNames) {
        if (this.roleDtoSet.isEmpty()) return false;
        for (String roleName : roleNames) {
            if (roleDtoSet.stream().noneMatch(x -> x.getRoleName().equalsIgnoreCase(roleName))) {
                return false;
            }
        }
        return true;
    }

    public Boolean hasRole(String roleName) {
        if (this.roleDtoSet.isEmpty()) return false;
        return roleDtoSet.stream()
                .anyMatch(x -> x.getRoleName()
                        .equalsIgnoreCase(roleName));
    }

    public Boolean hasAnyPermission(String... permissionNames) {
        if (this.roleDtoSet.isEmpty()) return false;
        for (String permissionName : permissionNames) {
            if (roleDtoSet.stream()
                    .flatMap(rs -> rs.getPermissionSet().stream())
                    .anyMatch(ps -> ps.getPermissionName().equalsIgnoreCase(permissionName))) {
                return true;
            }
        }
        return false;
    }

    public Boolean hasPermission(String permissionName) {
        if (this.roleDtoSet.isEmpty()) return false;
        return roleDtoSet.stream()
                .flatMap(rs -> rs.getPermissionSet().stream())
                .anyMatch(ps -> ps.getPermissionName()
                        .equalsIgnoreCase(permissionName));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roleDtoSet == null || roleDtoSet.isEmpty()) return null;
        return roleDtoSet.stream()
                .map(roleDto -> new SimpleGrantedAuthority(roleDto.getRoleName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status.isValid();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status.isValid();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status.isValid();
    }

    @Override
    public boolean isEnabled() {
        return this.status.isValid();
    }
}
