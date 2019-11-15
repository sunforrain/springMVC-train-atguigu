package com.atguigu.springmvc.test;

import com.atguigu.springmvc.crud.dao.EmployeeDao;
import com.atguigu.springmvc.crud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SpringMVCTest {
    @Autowired
    private EmployeeDao employeeDao;

    /**
     * 测试自定义转化器的handler方法
     * @param employee
     * @return
     */
    @RequestMapping("/testConversionServiceConverer")
    public String testConverter (@RequestParam("employee")Employee employee) {
        System.out.println("save: "+ employee);
        employeeDao.save(employee);
        // 注意要重定向
        return "redirect:emps";
    }

}
