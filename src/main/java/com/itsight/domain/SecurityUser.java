package com.itsight.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class SecurityUser {

	@Id
	@Column(name ="Username", unique = true, updatable = false)
	private String username;
	@Column(name = "Password", nullable = false)
	private String password;
	@Column(name = "Enabled", nullable = false)
	private boolean enabled;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "securityUser", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	private Set<SecurityRole> roles;
	
	public SecurityUser() {}
	
	public SecurityUser(String username, String password, boolean enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public SecurityUser(String username, String password, Set<SecurityRole> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<SecurityRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<SecurityRole> roles) {
		for (SecurityRole x : roles) {
			x.setSecurityUser(this);
		}
		this.roles = roles;
	}
	
}
