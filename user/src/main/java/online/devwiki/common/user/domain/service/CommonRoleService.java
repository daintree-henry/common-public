package online.devwiki.common.user.domain.service;

import online.devwiki.common.user.role.CommonRoleDto;

import java.util.Set;

public interface CommonRoleService {
    Set<CommonRoleDto> getAllRoles();

    CommonRoleDto getRoleById(Long roleId);

    Long createRole(String roleName);

}
