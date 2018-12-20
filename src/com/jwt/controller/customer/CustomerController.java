package com.jwt.controller.customer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jwt.common.utils.Page;
import com.jwt.po.BaseDict;
import com.jwt.po.Customer;
import com.jwt.po.User;
import com.jwt.service.baseDic.BaseDictService;
import com.jwt.service.customer.CustomerService;

@Controller
public class CustomerController {
	/**
	 * 注入实例业务
	 */
	@Autowired
	private CustomerService customerService;

	@Autowired
	private BaseDictService baseDictService;
	
	// 客户来源
	@Value("${customer.from.type}")
	private String FROM_TYPE;
	// 客户所属行业
	@Value("${customer.industry.type}")
	private String INDUSTRY_TYPE;
	// 客户级别
	@Value("${customer.level.type}")
	private String LEVEL_TYPE;
	/**
	 * 跳转到主页面
	 */
	@RequestMapping(value = "/customer/list.action")
	public String list(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows, String custName, 
			String custSource, String custIndustry, String custLevel, Model model) {
		//创建Map集合，将数据传入Map
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("page", page);
		conditionMap.put("rows", rows);
		conditionMap.put("custName", custName);
		conditionMap.put("custSource", custSource);
		conditionMap.put("custIndustry", custIndustry);
		conditionMap.put("custLevel", custLevel);
		Page<Customer> map = customerService.findCustomerList(conditionMap);
		model.addAttribute("page", map);
		// 客户来源
		List<BaseDict> fromType = baseDictService.findBaseDictByTypeCode(FROM_TYPE);
		// 客户所属行业
		List<BaseDict> industryType = baseDictService.findBaseDictByTypeCode(INDUSTRY_TYPE);
		// 客户级别
		List<BaseDict> levelType = baseDictService.findBaseDictByTypeCode(LEVEL_TYPE);
		// 添加参数
		model.addAttribute("fromType", fromType);
		model.addAttribute("industryType", industryType);
		model.addAttribute("levelType", levelType);
		model.addAttribute("custName", custName);
		model.addAttribute("custSource", custSource);
		model.addAttribute("custIndustry", custIndustry);
		model.addAttribute("custLevel", custLevel);
		return "customer";
	}
	/**
	 * 创建客户
	 */
	@RequestMapping("/customer/create.action")
	@ResponseBody
	public String customerCreate(Customer customer,HttpSession session) {
	    // 获取Session中的当前用户信息
	    User user = (User) session.getAttribute("USER_SESSION");
	    // 将当前用户id存储在客户对象中
	    customer.setCust_create_id(user.getUser_id());
	    // 创建Date对象
	    Date date = new Date();
	    // 得到一个Timestamp格式的时间，存入mysql中的时间格式“yyyy/MM/dd HH:mm:ss”
	    Timestamp timeStamp = new Timestamp(date.getTime());
	    customer.setCust_createtime(timeStamp);
	    // 执行Service层中的创建方法，返回的是受影响的行数
	    int rows = customerService.createCustomer(customer);
	    if(rows > 0){
	        return "OK";
	    }else{
	        return "FAIL";
	    }
	}

	/**
	 * 通过id获取客户信息
	 */
	@RequestMapping("/customer/getCustomerById.action")
	@ResponseBody
	public Customer getCustomerById(Integer id) {
	    Customer customer = customerService.getCustomerById(id);
	    return customer;
	}
	/**
	 * 更新客户
	 */
	@RequestMapping("/customer/update.action")
	@ResponseBody
	public String customerUpdate(Customer customer) {
	    int rows = customerService.updateCustomer(customer);
	    if(rows > 0){
	        return "OK";
	    }else{
	        return "FAIL";
	    }
	}

	/**
	 * 删除客户
	 */
	@RequestMapping("/customer/delete.action")
	@ResponseBody
	public String customerDelete(Integer id) {
	    int rows = customerService.deleteCustomer(id);
	    if(rows > 0){			
	        return "OK";
	    }else{
	        return "FAIL";			
	    }
	}
	
	/**
	 * 批量删除客户
	 */
	/**
	   * 批量删除
	   * @param request
	   * @param response
	   * @throws IOException 
	   */
	    @RequestMapping("/customer/selectDelect.action")
	    @ResponseBody
	    public String selectDelect(Integer[] ids){
	    	int rows = customerService.selectCustomer(ids);
	    	if(rows > 0){			
		        return "OK";
		    }else{
		        return "FAIL";			
		    }
	    }
	/** 
	 * 描述：通过 jquery.form.js 插件提供的ajax方式上传文件 
	 * @param request 
	 * @param response 
	 * @throws Exception 
	 */  
	@RequestMapping(value="/customer/ajaxUpload.action",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public String ajaxUploadExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {  
		return customerService.ajaxUploadExcel(request, response);
	} 
}
