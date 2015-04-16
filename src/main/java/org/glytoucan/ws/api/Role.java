package org.glytoucan.ws.api;

import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@XmlRootElement (name="role")
@JsonSerialize
public class Role {
	
	public final static String ADMIN="ADMIN";
	public final static String USER="USER";
	public final static String MODERATOR="MODERATOR";
	
	Integer roleId;
	String roleName;
	Set <User> users;
	
	public Role() {
	}
	
	public Role (String role) {
		this.roleName=role;
	}
	
	/**
	 * @return the roleId
	 */
	@XmlAttribute
	public Integer getRoleId() {
		return roleId;
	}
	
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
	/**
	 * @return the roleName
	 */
	@XmlAttribute
	public String getRoleName() {
		return roleName;
	}
	
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	/**
	 * @return the users
	 */
	@XmlTransient  // so that from the role we should not go back to users - prevent cycles
	@JsonIgnore
	public Set<User> getUsers() {
		return users;
	}
	
	/**
	 * @param users the users to set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}
}