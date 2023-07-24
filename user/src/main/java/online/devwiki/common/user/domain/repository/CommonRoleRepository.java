package online.devwiki.common.user.domain.repository;

import online.devwiki.common.user.domain.model.CommonRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonRoleRepository extends JpaRepository<CommonRole, Long> {
}
