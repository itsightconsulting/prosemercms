package com.itsight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.itsight.domain.SecurityUser;

@Repository
public interface SecurityUserRepository extends JpaRepository<SecurityUser, String> {
	
	SecurityUser findByUsername(String username);
	
	List<SecurityUser> findByRolesRoleLike(String nombre);
	
	@Query("UPDATE SecurityUser SET enabled = ?1  WHERE username = ?2")
	@Modifying
	@Transactional
	void saveUserStatusByUsername(boolean status, String username);

}
