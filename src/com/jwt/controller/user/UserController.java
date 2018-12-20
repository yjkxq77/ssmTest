package com.jwt.controller.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jwt.po.User;
import com.jwt.service.user.UserService;

@Controller
public class UserController {
	
	/*
	 * 注入业务实例
	 */
	@Autowired
	private UserService userService;
	
	/**
	 * 根据输入账号密码判断是否登录成功
	 */
	@RequestMapping(value = "/login.action", method = RequestMethod.POST)
	public String checkIfLogin(String usercode, String password, Model model, HttpSession session) {
		//通过账号密码查询用户
		/* String user_password = userService.checkIfLogin(code);
		 if(password.equals(user_password))
			 return "login";
		 else
		     return "fail";*/
		User user = userService.checkIfLogin(usercode, password);
		
		if(user != null) {
			//将用户添加到Session
			session.setAttribute("USER_SESSION", user);
			//跳转到主页面
			//return "fail";
			return "redirect:customer/list.action";
		}
		else {
		model.addAttribute("msg", "账号或密码有误，请重新输入！");
		return "login";
		}
	}
	/**
	 * 模拟其他类中跳转到客户管理页面的方法
	 */
	@RequestMapping(value = "/toCustomer.action")
	public String toCustomer() {
	    return "customer";
	}
	
	/**
	 * 退出登录
	 */
	@RequestMapping(value = "/logout.action")
	public String logout(HttpSession session) {
	    // 清除Session
	    session.invalidate();
	    // 重定向到登录页面的跳转方法
	    return "redirect:login.action";
	}
	/**
	 * 向用户登录页面跳转
	 */
	@RequestMapping(value = "/login.action", method = RequestMethod.GET)
	public String toLogin() {
	    return "login";
	}	
}
