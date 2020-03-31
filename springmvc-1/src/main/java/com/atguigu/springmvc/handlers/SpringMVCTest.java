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
// 任务16：SpringMVC_处理模型数据之SessionAttributes注解
@SessionAttributes(value = {"user"}, types = {String.class})
@RequestMapping("/springmvc")
@Controller
public class SpringMVCTest {

    private static final String SUCCESS = "success";

    // 任务28：SpringMVC_重定向
    // 转发或重定向
    @RequestMapping("/testRedirect")
    public String testRedirect(){
        System.out.println("testRedirect");
        // 如果返回的字符串中带 forward: 或 redirect: 前缀时，
        // SpringMVC 会对他们进行特殊处理
        // forward 执行转发, redirect执行重定向
        return "redirect:/index.jsp";
    }

    /**
     * 任务27：SpringMVC_自定义视图
     * 演示自定义解析器
     * @return
     */
    @RequestMapping("/testView")
    public String testView(){
        System.out.println("testView");
        // 返回的是HelloView.java处理的视图
        return "helloView";
    }

    /**
     * 任务24
     * 演示视图解析流程的方法
     * @return
     */
    @RequestMapping("/testViewAndViewResolver")
    public String testViewAndViewResolver(){
        System.out.println("testViewAndViewResolver");
        return SUCCESS;
    }

    /**
     * 任务18：SpringMVC_ModelAttribute注解之示例代码
     * 任务22：SpringMVC_ModelAttribute注解修饰POJO类型的入参
     * 1. 有 @ModelAttribute 标记的方法, 会在每个目标方法(有@RequestMapping的都会执行)执行之前被 SpringMVC 调用!
     * 2.  @ModelAttribute 注解也可以来修饰目标方法 POJO 类型的入参, 其 value 属性值有如下的作用:
     * 1). SpringMVC 会使用 value 属性值在 implicitModel 中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
     *      所以这里在@ModelAttribude标记的方法中,填个键是abc的,目标方法的入参也要指定value为abc
     * 2). SpringMVC 会以 value 为 key, POJO 类型的对象为 value, 存入到 request 中.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value="id",required=false) Integer id,
                        Map<String, Object> map){
        System.out.println("modelAttribute method");
        if(id != null){
            //模拟从数据库中获取对象
            User user = new User(1, "Tom", "123456", "tom@atguigu.com", 12);
            System.out.println("从数据库中获取一个对象: " + user);
            // 这里只要放的键是类名第一个字母小写,目标方法默认不用指定@ModelAttribute的value
            map.put("user", user);
        }
    }

    /**
     * 任务17：SpringMVC_ModelAttribute注解之使用场景
     * 场景见视频,看图好解释
     * 任务18：SpringMVC_ModelAttribute注解之示例代码
     * 注意上面的 getUser()
     * 运行流程:
     * 1. 执行 @ModelAttribute 注解修饰的方法: 从数据库中取出对象, 把对象放入到了 Map 中. 键为: user
     * 2. SpringMVC 从 Map 中取出 User 对象, 并把表单的请求参数赋给该 User 对象的对应属性.
     * 3. SpringMVC 把上述对象传入目标方法的参数.
     *
     * 注意: 在 @ModelAttribute 修饰的方法中, 放入到 Map 时的键需要和目标方法入参类型的第一个字母小写的字符串一致!
     *
     * 任务21：SpringMVC_如何确定目标方法POJO类型参数
     * SpringMVC 确定目标方法 POJO 类型入参的过程
     * 1. 确定一个 key:
     * 1). 若目标方法的 POJO 类型的参数木有使用 @ModelAttribute 作为修饰, 则 key 为 POJO 类名第一个字母的小写
     * 2). 若使用了  @ModelAttribute 来修饰, 则 key 为 @ModelAttribute 注解的 value 属性值.
     * 2. 在 implicitModel 中查找 key 对应的对象, 若存在, 则作为入参传入
     * 1). 若在 @ModelAttribute 标记的方法中在 Map 中保存过, 且 key 和 1 确定的 key 一致, 则会获取到.
     * 3. 若 implicitModel 中不存在 key 对应的对象, 则检查当前的 Handler 是否使用 @SessionAttributes 注解修饰,
     * 若使用了该注解, 且 @SessionAttributes 注解的 value 属性值中包含了 key, 则会从 HttpSession 中来获取 key 所
     * 对应的 value 值, 若存在则直接传入到目标方法的入参中. 若不存在则将抛出异常.
     *      如果需要用到@SessionAttributes,要解决这个异常,
     *      必须有@ModelAttribute修饰的方法先把@SessionAttributes中value指定的key先放到implicitModel中
     *      或者在目标方法中指定@ModelAttribute内的value与@SessionAttributes中value指定的key不一致
     * 4. 若 Handler 没有标识 @SessionAttributes 注解或 @SessionAttributes 注解的 value 值中不包含 key, 则
     * 会通过反射来创建 POJO 类型的参数, 传入为目标方法的参数
     * 5. SpringMVC 会把 key 和 POJO 类型的对象保存到 implicitModel 中, 进而会保存到 request 中.
     *
     * 任务20：SpringMVC_ModelAttribute注解之源码分析
     * 源代码分析的流程
     * 1. 调用 @ModelAttribute 注解修饰的方法. 实际上把 @ModelAttribute 方法中 Map 中的数据放在了 implicitModel 中.
     * 2. 解析请求处理器的目标参数, 实际上该目标参数来自于 WebDataBinder 对象的 target 属性
     * 1). 创建 WebDataBinder 对象:
     * ①. 确定 objectName 属性: 若传入的 attrName 属性值为 "", 则 objectName 为类名第一个字母小写.
     * 注意: attrName. 若目标方法的 POJO 属性使用了 @ModelAttribute 来修饰, 则 attrName 值即为 @ModelAttribute
     * 的 value 属性值
     *
     * ②. 确定 target 属性:
     * 	> 在 implicitModel 中查找 attrName 对应的属性值. 若存在, ok
     * 	> *若不存在: 则验证当前 Handler 是否使用了 @SessionAttributes 进行修饰, 若使用了, 则尝试从 Session 中
     * 获取 attrName 所对应的属性值. 若 session 中没有对应的属性值, 则抛出了异常.
     * 	> 若 Handler 没有使用 @SessionAttributes 进行修饰, 或 @SessionAttributes 中没有使用 value 值指定的 key
     * 和 attrName 相匹配, 则通过反射创建了 POJO 对象
     *
     * 2). SpringMVC 把表单的请求参数赋给了 WebDataBinder 的 target 对应的属性.
     * 3). *SpringMVC 会把 WebDataBinder 的 attrName 和 target 给到 implicitModel.
     * 进而传到 request 域对象中.
     * 4). 把 WebDataBinder 的 target 作为参数传递给目标方法的入参.
     */
    @RequestMapping("/testModelAttribute")
    public String testModelAttribute(@ModelAttribute("user") User user){
        // 由于前台没有传入password,没有增加@ModelAndView注解之前,user内会没有password,如果保存时存入空值显然是不对的
        System.out.println("修改: " + user);
        return SUCCESS;
    }

    /**
     * 任务16：SpringMVC_处理模型数据之SessionAttributes注解
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
     * 任务15：SpringMVC_处理模型数据之Map
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
     * 任务14：SpringMVC_处理模型数据之ModelAndView
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
     * 任务13：SpringMVC_使用Servlet原生API作为参数
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
     * 任务12：SpringMVC_使用POJO作为参数
     * Spring MVC 会按请求参数名和 POJO 属性名进行自动匹配， 自动为该对象填充属性值。支持级联属性。
     * 如：dept.deptId、dept.address.tel 等
     */
    @RequestMapping("/testPojo")
    public String testPojo(User user) {
        System.out.println("testPojo: " + user);
        return SUCCESS;
    }

    /**
     * 任务11：SpringMVC_CookieValue注解
     * 了解:
     * @CookieValue: 映射一个 Cookie 值. 属性同 @RequestParam
     * JSESSIONID 是示例中有的,比一定当真
     */
    @RequestMapping("/testCookieValue")
    public String testCookieValue(@CookieValue("JSESSIONID") String sessionId) {
        System.out.println("testCookieValue: sessionId: " + sessionId);
        return SUCCESS;
    }

    /**
     * 任务10：SpringMVC_RequestHeader注解
     * 了解: 映射请求头信息 用法同 @RequestParam
     */
    @RequestMapping("/testRequestHeader")
    public String testRequestHeader(
            @RequestHeader(value = "Accept-Language") String al) {
        System.out.println("testRequestHeader, Accept-Language: " + al);
        return SUCCESS;
    }

    /**
     * 任务9：SpringMVC_RequestParam注解
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
     * 任务8：SpringMVC_RequestMapping_HiddenHttpMethodFilter过滤器,PUT请求
     */
    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.PUT)
    public String testRestPut(@PathVariable Integer id) {
        System.out.println("testRest Put: " + id);
        return SUCCESS;
    }

    /**
     * 任务8：SpringMVC_RequestMapping_HiddenHttpMethodFilter过滤器,DELETE请求
     * @param id
     * @return
     */
    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.DELETE)
    public String testRestDelete(@PathVariable Integer id) {
        System.out.println("testRest Delete: " + id);
        return SUCCESS;
    }

    /**
     * 任务8：SpringMVC_RequestMapping_HiddenHttpMethodFilter过滤器,POST请求
     * @return
     */
    @RequestMapping(value = "/testRest", method = RequestMethod.POST)
    public String testRest() {
        System.out.println("testRest POST");
        return SUCCESS;
    }

    /**
     * 任务8：SpringMVC_RequestMapping_HiddenHttpMethodFilter过滤器,GET请求
     * @param id
     * @return
     */
    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.GET)
    public String testRest(@PathVariable Integer id) {
        System.out.println("testRest GET: " + id);
        return SUCCESS;
    }

    /**
     * 任务7：SpringMVC_RequestMapping_PathVariable注解
     * @PathVariable 可以来映射 URL 中的占位符到目标方法的参数中.
     * 注意映射的参数和PathVariable中的参数名需要一致
     * 该功能在SpringMVC 向 REST 目标挺进发展过程中具有里程碑的意义
     * @param id
     * @return
     */
    @RequestMapping("/testPathVariable/{id}")
    public String testPathVariable (@PathVariable("id") Integer id) {
        System.out.println("testPathVariable: " + id);
        return SUCCESS;
    }

    /**
     * 任务6：SpringMVC_RequestMapping_Ant路径
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
     * 任务5：SpringMVC_RequestMapping_请求参数&请求头
     * @RequestMapping 的 value、method、params 及 heads分别表示请求 URL、请求方法、请求参数及请求头的映射条件，
     *  他们之间是与的关系，
     * 了解: 可以使用 params 和 headers 来更加精确的映射请求. params 和 headers 支持简单的表达式.
     * 明确标注了RequestMapping的参数之后其实都是数组, params指定的参数如果缺少也会报错404
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
     * 任务4：SpringMVC_RequestMapping_请求方式
     * @RequestMapping 除了可以使用请求 URL 映射请求外，还可以使用请求方法、请求参数及请求头映射请求
     * 常用: 使用 method 属性来指定请求方式

     * 如果指定了POST方式,普通的点击连接就会报405错误
     */
    @RequestMapping(value = "/testMethod", method = RequestMethod.POST)
    public String testMethod () {
        System.out.println("testMethod");
        return SUCCESS;
    }

    /**
     * 任务3：SpringMVC_RequestMapping_修饰类
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
