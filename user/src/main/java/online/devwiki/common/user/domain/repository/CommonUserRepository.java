package online.devwiki.common.user.domain.repository;

import online.devwiki.common.user.domain.model.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonUserRepository extends JpaRepository<CommonUser, Long>, QuerydslPredicateExecutor<CommonUser> {

    Optional<CommonUser> findByLoginId(String loginId);
}

