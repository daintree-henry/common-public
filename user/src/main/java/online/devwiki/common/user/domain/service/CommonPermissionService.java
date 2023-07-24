package online.devwiki.common.user.domain.service;

import online.devwiki.common.user.role.CommonPermissionDto;

import java.util.Set;

public interface CommonPermissionService {

    Set<CommonPermissionDto> getAllPermissions();

    CommonPermissionDto getPermissionById(Long permissionId);

    Long createPermission(String name);

}
