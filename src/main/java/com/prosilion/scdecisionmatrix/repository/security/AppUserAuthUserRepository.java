package com.prosilion.scdecisionmatrix.repository.security;

import com.prosilion.scdecisionmatrix.model.entity.AppUserAuthUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserAuthUserRepository extends JpaRepository<AppUserAuthUser, Integer> {
	Optional<AppUserAuthUser> findByAppUserId(Integer appUserId);
	Optional<AppUserAuthUser> findByAuthUserName(String authUserName);

//	@Modifying(clearAutomatically = true)
//	@Query("UPDATE EscalationRequest set escalationId = :escalationId WHERE requestId = :requestId")
//	Integer update(Integer escalationId, Integer requestId);
}
