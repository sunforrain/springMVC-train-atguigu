package com.atguigu.springmvc.handlers;

import com.atguigu.springmvc.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

//@SessionAttributes(value = {"user"}, types = {String.class})
@RequestMapping("/springmvc")
@Controller
public class SpringMVCTest {

    private static final String SUCCESS = "success";

    /**
     * 1. 有 @ModelAttribute 标记的方法, 会在每个目标方法(有@RequestMapping的都会执行)执行之前被 SpringMVC 调用!
     * 2. @ModelAttribute 注解也可以来修饰目标方法 POJO 类型的入参, 其 value 属性值有如下的作用:
     * 1). SpringMVC 会使用 value 属性值在 implicitModel 中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
     * 2). SpringMVC 会一 value 为 key, POJO 类型的对象为 value, 存入到 request 中.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value="id",required=false) Integer id,
                        Map<String, Object> map){
        System.out.println("modelAttribute method");
        if(id != null){
            //模拟从数据库中获取对象
            User user = new User(1, "Tom", "123456", "tom@atguigu.com", 12);
            System.out.println("从数据库中获取一个对象: " + user);

            map.put("user", user);
        }
    }

    @RequestMapping("/testModelAttribute")
    public String testModelAttribute(User user){
        // 由于前台没有传入password,没有增加@ModelAndView注解之前,user内会没有password,如果保存时存入空值显然是不对的
        System.out.println("修改: " + user);
        return SUCCESS;
    }

    /**
     * @SessionAttributes 除了可以通过属性名指定需要放到会话中的属性外(实际上使用的是 value 属性值),
     * 还可以通过模型属性的对象类型指定哪些模型属性需要放到会话中(实际上使用的是 types 属性值)
     *
     * 注意: 该注解只能放在类的上面. 而不能修饰放方法上面.
     */
    @RequestMapping("/testSessionAttributes")
    public String testSessionAttributes(Map<String, Object> map){
        User user = new User("Tom", "123456", "tom@atguigu.com", 15);
        // user通过属性名放在会话中
        map.put("user", user);
        // 制定了String类型也能放在会话中,因而school也放进去了
        map.put("school", "atguigu");
        return SUCCESS;
    }

    /**
     * 目标方法可以添加 Map 类型(实际上也可以是 Model 类型或 ModelMap 类型)的参数.
     * map 实际是放在modelAndView里面的modelMap中
     * @param map
     * @return
     */
    @RequestMapping("/testMap")
    public String testMap(Map<String, Object> map){
        System.out.println(map.getClass().getName());
        map.put("names", Arrays.asList("Tom", "Jerry", "Mike"));
        return SUCCESS;
    }

    /**
     * 目标方法的返回值可以是 ModelAndView 类型。
     * 其中可以包含视图和模型信息
     * SpringMVC 会把 ModelAndView 的 model 中数据放入到 request 域对象中.
     * @return
     */
    @RequestMapping("/testModelAndView")
    public ModelAndView testModelAndView () {
        String viewName = SUCCESS;
        ModelAndView modelAndView = new ModelAndView(viewName);

        //添加模型数据到 ModelAndView 中.
        modelAndView.addObject("time", new Date());

        return modelAndView;
    }

    /**
     * 可以使用 Serlvet 原生的 API 作为目标方法的参数 具体支持以下类型
     *
     * HttpServletRequest
     * HttpServletResponse
     * HttpSession
     * java.security.Principal
     * Locale InputStream
     * OutputStream
     * Reader
     * Writer
     * @throws IOException
     */
    @RequestMapping("/testServletAPI")
    public void testServletAPI(HttpServletRequest request,
                               HttpServletResponse response, Writer out) throws IOException {
        System.out.println("testServletAPI, " + request + ", " + response);
        out.write("hello springmvc");
//		return SUCCESS;
    }

    /**
     * Spring MVC 会按请求参数名和 POJO 属性名进行自动匹配， 自动为该对象填充属性值。支持级联属性。
     * 如：dept.deptId、dept.address.tel 等
     */
    @RequestMapping("/testPojo")
    public String testPojo(User user) {
        System.out.println("testPojo: " + user);
        return SUCCESS;
    }

    /**
     * 了解:
     *
     * @CookieValue: 映射一个 Cookie 值. 属性同 @RequestParam
     */
    @RequestMapping("/testCookieValue")
    public String testCookieValue(@CookieValue("JSESSIONID") String sessionId) {
        System.out.println("testCookieValue: sessionId: " + sessionId);
        return SUCCESS;
    }

    /**
     * 了解: 映射请求头信息 用法同 @RequestParam
     */
    @RequestMapping("/testRequestHeader")
    public String testRequestHeader(
            @RequestHeader(value = "Accept-Language") String al) {
        System.out.println("testRequestHeader, Accept-Language: " + al);
        return SUCCESS;
    }

    /**
     * @RequestParam 来映射请求参数.
     *                  value 值即请求参数的参数名
     *                  required 该参数是否必须. 默认为 true, 如果不传会报500,改为false就不用强制传了
     *                  defaultValue 请求参数的默认值, 只设置非必须还不够,需要有默认参数值.否则会报400
     */
    @RequestMapping(value = "/testRequestParam")
    public String testRequestParam (
            @RequestParam(value = "username") String un,
            @RequestParam(value = "age", required = false, defaultValue = "0") int age ) {
        System.out.println("testRequestParam, username: " + un + ", age: "
                + age);
        return SUCCESS;
    }

    /**
     * Rest 风格的 URL. 以 CRUD 为例:
     * 新增: /order POST
     * 修改: /order/1 PUT update?id=1
     * 获取:/order/1 GET get?id=1
     * 删除: /order/1 DELETE delete?id=1
     *
     * 如何发送 PUT 请求和 DELETE 请求呢 ?
     * 1. 需要配置 HiddenHttpMethodFilter , 在web.xml中
     * 2. 需要发送 POST 请求
     * 3. 需要在发送 POST 请求时携带一个 name="_method" 的隐藏域, 值为 DELETE 或 PUT
     *
     * 在 SpringMVC 的目标方法中如何得到 id 呢?
     * 使用 @PathVariable 注解
     *
     */
    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.PUT)
    public String testRestPut(@PathVariable Integer id) {
        System.out.println("testRest Put: " + id);
        return SUCCESS;
    }

    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.DELETE)
    public String testRestDelete(@PathVariable Integer id) {
        System.out.println("testRest Delete: " + id);
        return SUCCESS;
    }

    @RequestMapping(value = "/testRest", method = RequestMethod.POST)
    public String testRest() {
        System.out.println("testRest POST");
        return SUCCESS;
    }

    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.GET)
    public String testRest(@PathVariable Integer id) {
        System.out.println("testRest GET: " + id);
        return SUCCESS;
    }

    /**
     * @PathVariable 可以来映射 URL 中的占位符到目标方法的参数中.
     * 注意映射的参数和PathVariable中的参数名需要一致
     * 该功能在SpringMVC 向 REST 目标挺进发展过程中具有里程碑的意义
     * 意义
     * @param id
     * @return
     */
    @RequestMapping("/testPathVariable/{id}")
    public String testPathVariable (@PathVariable("id") Integer id) {
        System.out.println("testPathVariable: " + id);
        return SUCCESS;
    }

    /**
     * Ant 风格资源地址支持 3 种匹配符：
     * ?：匹配文件名中的一个字符
     * *：匹配文件名中的任意字符
     * **：** 匹配多层路径
     * @return
     */
    @RequestMapping("/testAntPath/*/abc")
    public String testAntPath() {
        System.out.println("testAntPath");
        return SUCCESS;
    }

    /**
     * 了解: 可以使用 params 和 headers 来更加精确的映射请求. params 和 headers 支持简单的表达式.
     * 明确标注了RequestMapping的参数之后其实都是数组
     * @return
     */
    @RequestMapping(value = "/testParamsAndHeaders",
            params = {"username", "age != 11"},
            headers = { "Accept-Language=zh-CN,zh;q=0.8" })  // 中文环境这个请求头一般是zh-CN,zh;q=0.8
    public String testParamAndHeaders () {
        System.out.println("testParamAndHeaders");
        return SUCCESS;
    }

    /**
     * 常用: 使用 method 属性来指定请求方式
     * 如果指定了POST方式,普通的点击连接就会报405错误
     */
    @RequestMapping(value = "/testMethod", method = RequestMethod.POST)
    public String testMethod () {
        System.out.println("testMethod");
        return SUCCESS;
    }

    /**
     * 1. @RequestMapping 除了修饰方法, 还可来修饰类
     * 2. 1). 类定义处: 提供初步的请求映射信息。相对于 WEB 应用的根目录,注意这里在网页上不用写/
     *    2). 方法处: 提供进一步的细分映射信息。
     *          相对于类定义处的 URL。若类定义处未标注 @RequestMapping，则方法处标记的 URL
     *          相对于 WEB 应用的根目录
     */
    @RequestMapping("/testRequestMapping")
    public String testRequestMapping () {
        System.out.println("testRequestMapping");
        return SUCCESS;
    }
}
