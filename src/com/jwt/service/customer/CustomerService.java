package com.jwt.service.customer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jwt.common.utils.Page;
import com.jwt.po.Customer;
public interface CustomerService 
{
	// 查询客户列表
	public Page<Customer> findCustomerList(Map<String,Object> conditionMap);
	
	public int createCustomer(Customer customer);
	
	// 通过id查询客户
	public Customer getCustomerById(Integer id);
	
	// 更新客户
	public int updateCustomer(Customer customer);
	
	// 删除客户
	public int deleteCustomer(Integer id);

	public Integer selectCustomer(Integer[] delitems);
	// 导入Excel
	public String ajaxUploadExcel(HttpServletRequest request,HttpServletResponse response);

}
