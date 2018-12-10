package com.itsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.SecurityRole;

@Repository
@Transactional
public interface SecurityRoleRepository extends JpaRepository<SecurityRole, Integer> {
	
	SecurityRole findBySecurityUserUsername(String username);
}
