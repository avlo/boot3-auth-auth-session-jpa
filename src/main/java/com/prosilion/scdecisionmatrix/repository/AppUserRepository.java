package com.prosilion.scdecisionmatrix.repository;

import com.prosilion.scdecisionmatrix.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
}
