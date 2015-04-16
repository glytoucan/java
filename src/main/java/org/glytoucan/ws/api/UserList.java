package org.glytoucan.ws.api;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import com.wordnik.swagger.annotations.ApiModel;

/**
 * Wrapper for a list of users
 * Needed for creating meaningful xml elements for collections uf users
 * 
 * @author sena
 *
 */

@XmlRootElement(name="users")      // when XML is generated, the list of users will be under "users" tag
@ApiModel (value="UserList", description="List of Users")
public class UserList {
	private List<User> users;
	
	public UserList() {
		this.users = new ArrayList<User>();
	}
	
	public UserList (List<User> usrs) {
		this.users = usrs;
	}

	@XmlElement(name="user")       // each user will be inside "user" element in XML
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
}
