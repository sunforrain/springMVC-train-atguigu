package com.atguigu.springmvc.converters;

import com.atguigu.springmvc.crud.dao.DepartmentDao;
import com.atguigu.springmvc.crud.entities.Department;
import com.atguigu.springmvc.crud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * 自定义转换器
 * 直接将一个字符串转换成一个employee对象
 * 字符串格式为
 * lastname-email-gender-department.id 例如: GG-gg@atguigu.com-0-105
 */
@Component
public class EmployeeConverter implements Converter<String, Employee> {
    @Autowired
    private DepartmentDao departmentDao;
    /**
     * 需要实现的转换方法
     * @param source 即lastname-email-gender-department.id  格式的字符串
     * @return
     */
    @Override
    public Employee convert(String source) {
        if (source != null) {
            // 分割字符串中的 - ,变成一个数组
            String[] vals = source.split("-");
            if (vals != null && vals.length == 4) {
                String lastName = vals[0];
                String email = vals[1];
                Integer gender = Integer.parseInt(vals[2]);
                Department department = departmentDao.getDepartment(Integer.parseInt(vals[3]));

                Employee employee = new Employee(null, lastName, email, gender, department);

                System.out.println(source + "--convert--" + employee);
                return employee;
            }
        }
        return null;
    }
}
