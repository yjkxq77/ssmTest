package com.jwt.service.user;

import com.jwt.po.User;

public interface UserService {
	public User checkIfLogin(String user_code, String user_password);
}
