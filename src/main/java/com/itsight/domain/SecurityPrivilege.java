package com.itsight.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


@Entity
@Table(uniqueConstraints = @UniqueConstraint( columnNames = { "Privilege","SecurityRoleId"}))
public class SecurityPrivilege {
	
	@Id
	@GeneratedValue(generator="security_privilege_seq")
	@GenericGenerator(
	        name = "security_privilege_seq",
	    	        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
	        parameters = {
	        		@Parameter(name="prefer_sequence_per_entity", value="true"),
	                @Parameter(name = "security_privilege_seq", value = "security_privilege_seq"),
	                @Parameter(name = "initial_value", value = "1"),
	                @Parameter(name = "increment_size", value = "1")
	        }
	)
	@Column(name = "SecurityPrivilegeId")
	private int id;
	
	@Column(name = "Privilege" ,nullable = false)
	private String privilege;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SecurityRoleId")
	private SecurityRole securityRole;
	
	public SecurityPrivilege() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public SecurityRole getSecurityRole() {
		return securityRole;
	}

	public void setSecurityRole(SecurityRole securityRole) {
		this.securityRole = securityRole;
	}
	
}
