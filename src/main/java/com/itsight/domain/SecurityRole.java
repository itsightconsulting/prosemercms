package com.itsight.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"Role","Username"}))
public class SecurityRole {

	@Id
	@GeneratedValue(generator="security_role_seq")
	@GenericGenerator(
	        name = "security_role_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "security_role_seq", value = "security_role_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "SecurityRoleId")
	private int id;
	@Column(name = "Role", nullable = false)
	private String role;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Username")
	private SecurityUser securityUser;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "securityRole", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	private Set<SecurityPrivilege> privileges;
	
	public SecurityRole() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	public Set<SecurityPrivilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<SecurityPrivilege> privileges) {
		this.privileges = privileges;
	}
	
	
	
	
	

}
