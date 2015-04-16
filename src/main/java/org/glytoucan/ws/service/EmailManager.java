package org.glytoucan.ws.service;

import java.util.List;

import org.glytoucan.ws.api.User;


public interface EmailManager {
	void sendPasswordReminder (User user);
	void sendUserQuotaAlert (List<User> moderators, String userName);
	void sendUserRegistered (List<User> moderators, String userName);
}