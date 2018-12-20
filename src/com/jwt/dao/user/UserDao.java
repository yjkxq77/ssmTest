package com.jwt.dao.user;

import org.apache.ibatis.annotations.Param;

import com.jwt.po.User;

/**
 * User接口文件
 */
public interface UserDao {
	/**
	 * 用户登录
	 * @return 
	 */
	public User checkIfLogin(@Param("user_code")String user_code, @Param("user_password")String user_password);

}
