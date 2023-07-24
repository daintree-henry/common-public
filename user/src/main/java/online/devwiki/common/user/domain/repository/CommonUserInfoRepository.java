package online.devwiki.common.user.domain.repository;

import online.devwiki.common.user.domain.model.CommonUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonUserInfoRepository extends JpaRepository<CommonUserInfo, Long>, QuerydslPredicateExecutor<CommonUserInfo> {
}
