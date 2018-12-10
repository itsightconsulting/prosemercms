package com.itsight.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itsight.domain.SecurityPrivilege;
import com.itsight.domain.SecurityRole;
import com.itsight.domain.SecurityUser;
import com.itsight.repository.SecurityUserRepository;

import javax.servlet.http.HttpSession;

@Service
public class SecurityServiceImpl implements UserDetailsService{
	
	private static final Logger LOGGER = LogManager.getLogger(SecurityServiceImpl.class);
	
	@Autowired
	private SecurityUserRepository securityUserRepository;

	@Autowired
	private HttpSession session;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		try {
			if((username.substring(username.length()-5).equals(String.valueOf(session.getAttribute("codCaptcha"))))){
				SecurityUser user = securityUserRepository.findByUsername(username.substring(0, username.length()-5));
				if(user != null) {
					return buildUser(user, buildAuthorities(user.getRoles()));
				}
			} else{
				throw new UsernameNotFoundException("UsernameNotFoundException | (?): "+username.toUpperCase());
			}

		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("> Excepción | (?): " + username.toUpperCase());
		}
		LOGGER.info("> UsernameNotFoundException | (?): "+username.toUpperCase());
		throw new UsernameNotFoundException("UsernameNotFoundException | (?): "+username.toUpperCase());
	}
	
	private User buildUser(SecurityUser securityUser,Set<GrantedAuthority> lstRole) {
		return 
				new User(securityUser.getUsername(),
						 securityUser.getPassword(),
						 securityUser.isEnabled(),
						 true,
						 true,
						 securityUser.isEnabled(), lstRole);
	}
	
	private Set<GrantedAuthority> buildAuthorities(Set<SecurityRole> roles){
		Set<GrantedAuthority> lstRole = new HashSet<>();
		
		for(SecurityRole role: roles) {
			LOGGER.debug("> USER ROLE: " + role.getRole());
			lstRole.add(new SimpleGrantedAuthority(role.getRole()));
			for (SecurityPrivilege privilege : role.getPrivileges()) {
				lstRole.add(new SimpleGrantedAuthority(privilege.getPrivilege()));
				LOGGER.debug("> USER PRIVILEGE: " + privilege.getPrivilege());
			}
		}
		return lstRole;
	}

	
}
