package org.glytoucan.ws.service;

import java.util.Date;
import java.util.List;

import org.glytoucan.ws.api.User;
import org.glytoucan.ws.api.UserInput;

public interface UserManager {

	public void addUser(User user);
	public void modifyUser (User user);
	public List<User> getAllUsers();
	public List<User> getUsers (boolean validated, boolean active);
	
	public List<User> getUsersByValidated (boolean validated);
	public List<User> getUsersByActive (boolean active);
	
	public void activateUser (Integer userId);
	public void deactivateUser (Integer userId);
	public void validateUser (Integer userId);
	public void deleteUser (Integer userId);
	public void addRole(Integer userId, String roleName);
	public User getUser (Integer userId);
	public User getUserByLoginId (String loginId, boolean checkValidated, boolean checkActive);
	public User getUserByOpenIdLogin (String openId, boolean checkValidated, boolean checkActive);
	public String recoverLogin(String email);
	public UserInput recoverPassword (String loginId);
	public void changePassword (String loginId, String newPassword);
	public void setLoggedinDate (User user, Date loginDate);
	
	//public boolean login (String loginId, String password);
	void removeRole(Integer userId, String roleName);
	List<String> getRoles ();
	public List<User> getModerators();
	
	public void updateUserQuota (Integer userId, Integer newQuota);
	
}
