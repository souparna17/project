package com.petcure.dto;

import java.util.List;

import com.petcure.entity.User;

import lombok.Data;

public class UsersResponseDto extends CommanApiResponse {

	private List<User> users;

	private User user;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
