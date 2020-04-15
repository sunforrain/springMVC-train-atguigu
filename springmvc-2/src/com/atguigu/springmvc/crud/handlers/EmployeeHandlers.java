package com.atguigu.springmvc.crud.handlers;

import com.atguigu.springmvc.crud.dao.DepartmentDao;
import com.atguigu.springmvc.crud.dao.EmployeeDao;
import com.atguigu.springmvc.crud.entities.Department;
import com.atguigu.springmvc.crud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class EmployeeHandlers {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;



    /**
     * 更新employee需要用到的@ModelAttribute方法,防止存进去空值
     * @param id
     * @param map
     */
    @ModelAttribute
    public void getEmployee (@RequestParam(value = "id", required = false) Integer id,
                             Map<String, Object> map) {
        if(id != null) {
            map.put("employee", employeeDao.get(id));
        }
    }

    /**
     * 更新employee
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.PUT)
    public String update (Employee employee) {
        System.out.println("update:" + employee);
        employeeDao.save(employee);
        return "redirect:/emps";
    }

    /**
     * 更改employee前,获得反显数据并转到imput视图
     * 注意点:
     *  1. update与save的input方法不能写一起,因为用于获取id这个参数在save中没有
     *  2. 页面因为是复用的,因而需要c:if 来处理
     *  3. 提交时需要将前台的请求方法转为PUT,见页面的隐藏域
     *  4. 因为前台不传lastName,需要@ModelAttribute修饰的方法获取到要修改的employee,防止lastName为空保存了
     *  5. update的表单action建议使用绝对路径 action="${pageContext.request.contextPath }/emp" ,相对路径有问题
     * @param id
     * @param map
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    public String input (@PathVariable("id") Integer id, Map<String, Object> map) {
        map.put("employee", employeeDao.get(id));
        map.put("departments", departmentDao.getDepartments());
        return "input";
    }

    /**
     * 任务32：SpringMVC_RESTRUL_CRUD_删除操作&处理静态资源
     * 删除employee
     * 注意点:
     *  1. handler目标方法测参数使用PathVariable从页面链接获取id
     *  2. springmvc.xml要配置mvc:default-servlet-handler 和 mvc:annotation-driven
     *      处理静态资源的映射
     *  3. 使用jQuery方法处理请求为POST隐藏_method为DELETE
     * @param id
     * @return
     */
    @RequestMapping(value="/emp/{id}", method=RequestMethod.DELETE)
    public String delete(@PathVariable("id") Integer id){
        employeeDao.delete(id);
        return "redirect:/emps";
    }

    /**
     * 任务31：SpringMVC_RESTRUL_CRUD_添加操作&表单标签
     * 保存employee的方法,保存操作使用POST
     * 注意点:
     * 1. 保存相关的两个目标方法,URL是一样的,method不同
     * 2. 在添加页面需要用到的级联属性,如这里的department,要提前添加到map中
     * 3.关于springMVC的表单标签
     *      1) 注意指定表单的modelAttribute,来指定请求域的bean,默认是command
     *      2) 多选中items的使用
     *      3) path属性的级联
     * 4.@Valid 让 Spring MVC 在对指定的入参完成数据绑定后执行数据校验的工作
     * 5.需校验的 Bean 对象和其绑定结果对象或错误对象时成对出现的，它们之间不允许声明其他的入参
     * @param employee
     * @param bindingResult 数据转换出现错误,错误信息会放到BindingResult中
     * @return
     */
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    public String save(@Valid Employee employee, BindingResult bindingResult, Map<String, Object> map) {
        System.out.println("save:" + employee);
        // 如果有错误信息,打印出来,这只是个初步的写法,数据转换有错误默认不会弹异常
        if (bindingResult.getErrorCount() > 0) {
            System.out.println("出错了!");
            // getFieldErrors可以获得错误的list
            for(FieldError error: bindingResult.getFieldErrors()) {
                System.out.println(error.getField() + ":" + error.getDefaultMessage());
            }
            map.put("departments",departmentDao.getDepartments());
            return "input";
        }
        employeeDao.save(employee);
        return "redirect:/emps";
    }

    /**
     * 任务31：SpringMVC_RESTRUL_CRUD_添加操作&表单标签
     * 跳转到添加employee 的页面的目标方法,需要指定method为get,我们是RESTFUL的
     * @param map
     * @return
     */
    @RequestMapping(value="/emp", method=RequestMethod.GET)
    public String input(Map<String, Object> map){
        // 在添加页面需要用到的级联属性,如这里的department,要提前添加到map中
        map.put("departments", departmentDao.getDepartments());
        // 指定请求域的bean,要提前添加到map中
        map.put("employee", new Employee());
        return "input";
    }

    /**
     * 任务30：SpringMVC_RESTRUL_CRUD_显示所有员工信息
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

    /**
     * @InitBinder可以用于对 WebDataBinder 对象进行初始化。
     * WebDataBinder 是 DataBinder 的子类，用于完成由表单字段到 JavaBean 属性的绑定
     * @param binder
     */
//    @InitBinder
//	public void initBinder(WebDataBinder binder){
//        // 这里设置对lastName属性不进行映射.
//        // 实际应用中可能对于角色这种,前台传进来是一个 , 分隔的字符串,后台实体类是一个集合,
//        // 就需要不让springMVC自动映射
//		binder.setDisallowedFields("lastName");
//	}
}
