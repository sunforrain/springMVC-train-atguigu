package com.atguigu.springmvc.crud.handlers;

import com.atguigu.springmvc.crud.dao.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class EmployeeHandlers {

    @Autowired
    private EmployeeDao employeeDao;

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
