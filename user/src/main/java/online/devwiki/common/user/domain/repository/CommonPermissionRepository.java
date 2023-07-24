package online.devwiki.common.user.domain.repository;

import online.devwiki.common.user.domain.model.CommonPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonPermissionRepository extends JpaRepository<CommonPermission, Long> {
}
