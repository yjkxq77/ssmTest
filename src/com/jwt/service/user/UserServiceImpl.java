package com.jwt.service.user;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwt.dao.user.UserDao;
import com.jwt.po.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	//注入UserDao
	@Autowired
	private UserDao userDao;

	static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	//判断登录
	@Override
	public User checkIfLogin(String user_code, String user_password) {
		//测试日志
		logger.debug("日志测试");
		return this.userDao.checkIfLogin(user_code, user_password);
		
	}
}
