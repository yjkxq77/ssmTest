package com.jwt.service.customer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.jwt.common.utils.ExcelUtils;
import com.jwt.common.utils.Page;
import com.jwt.po.Customer;
import com.jwt.dao.customer.CustomerDao;

/**
 * 客户管理
 */
@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService{
	/**
	 * 声明DAO属性并注入
	 */
	@Autowired
	private CustomerDao customerDao;
	
	// 客户列表
	public Page<Customer> findCustomerList(Map<String, Object> conditionMap)
	{
		//创建客户对象
		Customer customer = new Customer();
		//判断客户名称
		if(StringUtils.isNotBlank((CharSequence) conditionMap.get("custName"))) {
			customer.setCust_name((String) conditionMap.get("custName"));
		}
		//判断信息来源
		if(StringUtils.isNotBlank((CharSequence) conditionMap.get("custSource"))) {
			customer.setCust_source((String) conditionMap.get("custSource"));
		}
		// 判断客户所属行业
		if(StringUtils.isNotBlank((CharSequence) conditionMap.get("custIndustry"))) {
			customer.setCust_industry((String) conditionMap.get("custIndustry"));
		}
		// 判断客户级别
		if(StringUtils.isNotBlank((CharSequence) conditionMap.get("custLevel"))) {
			customer.setCust_level((String) conditionMap.get("custLevel"));
		}
		// 当前页
		customer.setStart(((Integer) conditionMap.get("page")-1)*(Integer) conditionMap.get("rows")) ;
		// 每页数
		customer.setRows((Integer) conditionMap.get("rows"));
		// 查询客户列表
		List<Customer> customers = customerDao.selectCustomerList(customer);
		// 查询客户列表总记录数
		Integer count = customerDao.selectCustomerListCount(customer);
		// 创建Page返回对象
		Page<Customer> result = new Page<>();
		result.setPage((Integer) conditionMap.get("page"));
		result.setRows(customers);
		result.setSize((Integer) conditionMap.get("rows"));
		result.setTotal(count);
		return result;
	}
	
	/**
	 * 创建客户
	 */
	@Override
	public int createCustomer(Customer customer) {
	    return customerDao.createCustomer(customer);
	}
	/**
	 * 通过id查询客户
	 */
	@Override
	public Customer getCustomerById(Integer id) {	
	    Customer customer = customerDao.getCustomerById(id);
	    return customer;		
	}
	/**
	 * 更新客户
	 */
	@Override
	public int updateCustomer(Customer customer) {
	    return customerDao.updateCustomer(customer);
	}
	/**
	 * 删除客户
	 */
	@Override
	public int deleteCustomer(Integer id) {
	    return customerDao.deleteCustomer(id);	
	}

	/**
	 * 批量删除客户
	 */
	@Override
	public Integer selectCustomer(Integer[] delitems) {
		try {
			for(int i = 0; i < delitems.length; i++) {
				customerDao.deleteCustomer(delitems[i]);
			}
		} catch (Exception e) {
			return 0;
		}
		return 1;
	}
	
	/**
	 * 导入Excel文件
	 */
	@Override
	public String ajaxUploadExcel(HttpServletRequest request, HttpServletResponse response) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
        MultipartFile file = multipartRequest.getFile("upfile");  
        if(file.isEmpty()){  
            try {
				throw new Exception("文件不存在！");
			} catch (Exception e) {
				e.printStackTrace();
			}  
        }  
        InputStream in =null;  
        try {
			in = file.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}   
		List<List<Object>> listob = null; 
		try {
			listob = new ExcelUtils().getBankListByExcel(in,file.getOriginalFilename());
		} catch (Exception e) {
			e.printStackTrace();
		}   
		
	    //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出  
        for (int i = 0; i < listob.size(); i++) {  
            List<Object> lo = listob.get(i);  
            Customer customer = new Customer(); 
            Customer j = null;
        	
			try {
				j = customerDao.selectByPrimaryKey(Integer.valueOf(String.valueOf(lo.get(0))));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("没有新增");
			}
			customer.setCust_id(Integer.valueOf(String.valueOf(lo.get(0))));  
			customer.setCust_name(String.valueOf(lo.get(1)));
			customer.setCust_user_id(Integer.valueOf(String.valueOf(lo.get(2))));  
			customer.setCust_create_id(Integer.valueOf(String.valueOf(lo.get(3)))); 
			customer.setCust_source(String.valueOf(lo.get(4)));
			customer.setCust_industry(String.valueOf(lo.get(5)));
			customer.setCust_level(String.valueOf(lo.get(6)));
			customer.setCust_linkman(String.valueOf(lo.get(7)));
			customer.setCust_phone(String.valueOf(lo.get(8)));
			customer.setCust_mobile(String.valueOf(lo.get(9)));
			customer.setCust_zipcode(String.valueOf(lo.get(10)));
			customer.setCust_address(String.valueOf(lo.get(11)));
			customer.setCust_createtime(Date.valueOf((String) lo.get(4)));
			if(j == null)
			{
				customerDao.insert(customer);
			}
			else
			{
				customerDao.updateByPrimaryKey(customer);
			}
        }   
        return "文件导入成功！";
	}

}
