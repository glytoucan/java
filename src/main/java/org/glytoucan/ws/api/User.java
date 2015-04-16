package org.glytoucan.ws.api;

import java.util.Date;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 */
@XmlRootElement (name="user")
@JsonSerialize
@JsonIgnoreProperties({"password", "openIdLogin", "quota"})
public class User {
	
	public static int DEFAULT_QUOTA = 10;
	
	int userId;
	String userName;
	String openIdLogin;
	String loginId;
	String email;
	String password;
	Boolean active = true;
	Boolean validated = false;
	String affiliation;
	Date dateRegistered;
	Date lastLoggedIn;
	Set<Role> roles;
	Integer quota = DEFAULT_QUOTA;
	

	@XmlAttribute
	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	@JsonSerialize(using = DateSerializer.class)
	@XmlAttribute
	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	
	@JsonSerialize(using = DateSerializer.class)
	@XmlAttribute
	public Date getLastLoggedIn() {
		return lastLoggedIn;
	}

	public void setLastLoggedIn(Date lastLoggedIn) {
		this.lastLoggedIn = lastLoggedIn;
	}
	
	/**
	 * 
	 * @return the email associated with the user
	 */
	@XmlAttribute
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email email of the user
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return password for the user
	 */
	@JsonIgnore
	@XmlTransient
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password password to be set for the user
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return whether this user is active
	 */
	@XmlAttribute
	public Boolean getActive() {
		return active;
	}

	/**
	 * 
	 * @param active whether this user is active/inactive
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	
	/**
	 * 
	 * @return login name for the user
	 */
	
	@XmlAttribute
	public String getLoginId() {
		return loginId;
	}
	
	/**
	 * 
	 * @param loginId login name for the user
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	
	/**
	 * 
	 * @return assigned user id for the user
	 */
	@XmlAttribute
	public int getUserId() {
		return userId;
	}
	
	/**
	 * 
	 * @param userid userid of the user
	 */
	public void setUserId(int userid) {
		this.userId = userid;
	}
	
	/**
	 * 
	 * @return user's full name
	 */
	@XmlAttribute
	public String getUserName() {
		return userName;
	}
	
	/**
	 * 
	 * @param userName user's full name
	 */
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * @return the roles
	 */
	public Set<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * @return the validated
	 */
	@XmlAttribute
	public Boolean getValidated() {
		return validated;
	}

	/**
	 * @param validated the validated to set
	 */
	public void setValidated(Boolean validated) {
		this.validated = validated;
	}
	
	/**
	 * @return the openIdLogin
	 */
	@XmlTransient
	public String getOpenIdLogin() {
		return openIdLogin;
	}

	/**
	 * @param openIdLogin the openIdLogin to set
	 */
	public void setOpenIdLogin(String openIdLogin) {
		this.openIdLogin = openIdLogin;
	}

	/**
	 * 
	 * @return the quota
	 */
	@XmlTransient
	public Integer getQuota() {
		return quota;
	}

	public void setQuota(Integer quota) {
		this.quota = quota;
	}
}
