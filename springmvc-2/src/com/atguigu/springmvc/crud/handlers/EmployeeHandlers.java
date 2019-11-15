package com.atguigu.springmvc.crud.handlers;

import com.atguigu.springmvc.crud.dao.DepartmentDao;
import com.atguigu.springmvc.crud.dao.EmployeeDao;
import com.atguigu.springmvc.crud.entities.Department;
import com.atguigu.springmvc.crud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class EmployeeHandlers {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;

    /**
     * 跳转到添加页面的目标方法,需要指定method为get,我们是RESTFUL的
     * @param map
     * @return
     */
    @RequestMapping(value="/emp", method=RequestMethod.GET)
    public String input(Map<String, Object> map){
        map.put("departments", departmentDao.getDepartments());
        map.put("employee", new Employee());
        return "input";
    }

    /**
     * 简单遍历 employee的list
     * @param map
     * @return
     */
    @RequestMapping("/emps")
    public String list(Map<String, Object> map) {
        // 放到方法参数的map内,springMVC会帮我们放到request中,但是jsp页面内需要用到jstl
        map.put("employees", employeeDao.getAll());
        return "list";
    }
}
