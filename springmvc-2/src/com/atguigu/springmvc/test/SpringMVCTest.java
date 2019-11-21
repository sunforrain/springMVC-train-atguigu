package com.atguigu.springmvc.test;

import com.atguigu.springmvc.crud.dao.EmployeeDao;
import com.atguigu.springmvc.crud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

@Controller
public class SpringMVCTest {
    @Autowired
    private EmployeeDao employeeDao;

    // 处理国际化需要注入ResourceBundleMessageSource的实例
    @Autowired
    private ResourceBundleMessageSource messageSource;

    /**
     * 演示SimpleMappingExceptionResolver
     * 这里用前台传的数大于10,造了个数组下标越界异常
     *
     * 如果希望对所有异常进行统一处理，可以使用SimpleMappingExceptionResolver，
     * 它将异常类名映射为视图名，即发生异常时使用对应的视图报告异常
     *
     * SimpleMappingExceptionResolver是这里演示的异常处理类中唯一一个需要在配置文件中配置的
     * @param i
     * @return
     */
    @RequestMapping("/testSimpleMappingExceptionResolver")
    public String testSimpleMappingExceptionResolver(@RequestParam("i") int i){
        String [] vals = new String[10];
        System.out.println(vals[i]);
        return "success";
    }

    /**
     * 演示DefaultHandlerExceptionResolver
     * 具体包含哪些异常可以看源码
     * 这里前台传的是GET请求,人为制造一个HttpRequestMethodNotSupportedException
     * @return
     */
    @RequestMapping(value="/testDefaultHandlerExceptionResolver",method=RequestMethod.POST)
    public String testDefaultHandlerExceptionResolver(){
        System.out.println("testDefaultHandlerExceptionResolver...");
        return "success";
    }

    /**
     * 演示ResponseStatusExceptionResolver
     * @ResponseStatus 使用的演示,放在目标方法上,无论有没有异常都会返回对应的状态码
     * @param i
     * @return
     */
    @ResponseStatus(reason="测试",value=HttpStatus.NOT_FOUND)
    @RequestMapping("/testResponseStatusExceptionResolver")
    public String testResponseStatusExceptionResolver(@RequestParam("i") int i){
        // 传入的参数是13的时候弹我们自定义的异常
        if(i == 13){
            throw new UserNameNotMatchPasswordException();
        }
        System.out.println("testResponseStatusExceptionResolver...");

        return "success";
    }

    //	@ExceptionHandler({RuntimeException.class})
//	public ModelAndView handleArithmeticException2(Exception ex){
//		System.out.println("[出异常了]: " + ex);
//		ModelAndView mv = new ModelAndView("error");
//		mv.addObject("exception", ex);
//		return mv;
//	}

    /**
     * 1. 在 @ExceptionHandler 方法的入参中可以加入 Exception 类型的参数, 该参数即对应发生的异常对象
     * 2. @ExceptionHandler 方法的入参中不能传入 Map. 若希望把异常信息传导页面上, 需要使用 ModelAndView 作为返回值
     * 3. @ExceptionHandler 方法标记的异常有优先级的问题.优先用最接近的异常的处理方法
     * 4. @ControllerAdvice: 如果在当前 Handler 中找不到 @ExceptionHandler 方法来出来当前方法出现的异常,
     * 则将去 @ControllerAdvice 标记的类中查找 @ExceptionHandler 标记的方法来处理异常.
     */
//	@ExceptionHandler({ArithmeticException.class})
//	public ModelAndView handleArithmeticException(Exception ex){
//		System.out.println("出异常了: " + ex);
//		ModelAndView mv = new ModelAndView("error");
//		mv.addObject("exception", ex);
//		return mv;
//	}

    @RequestMapping("/testExceptionHandlerExceptionResolver")
    public String testExceptionHandlerExceptionResolver(@RequestParam("i") int i){
        System.out.println("result: " + (10 / i));
        return "success";
    }

    /**
     * 文件上传的测试,这个是正经的上传
     * @param desc
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping("/testFileUpload")
    public String testFileUpload(@RequestParam("desc") String desc,
                                 @RequestParam("file") MultipartFile file) throws IOException{
        System.out.println("desc: " + desc);
        System.out.println("OriginalFilename: " + file.getOriginalFilename());
        System.out.println("InputStream: " + file.getInputStream());
        return "success";
    }

    /**
     * 将i18n这个链接改为在handler处理国际化,注意区分这里和用jstl标签处理的区别
     * @param locale 与浏览器的语言设置相关
     * @return
     */
    @RequestMapping("/i18n")
    public String testI18n(Locale locale){
        String val = messageSource.getMessage("i18n.user", null, locale);
        System.out.println(val);
        return "i18n";
    }

    /**
     * ttpMessageConverter实现类的测试方法2
     * ResponseEntity<T> 类型作为返回值的示例
     * 这里模拟了下载文件,直接用输入流读取服务器上的文件
     * 然后使用ByteArrayHttpMessageConverter进行的读写操作
     * @param session
     * @return
     * @throws IOException
     */
    @RequestMapping("/testResponseEntity")
    public ResponseEntity<byte[]> testResponseEntity (HttpSession session) throws IOException {
        byte [] body = null;
        // 通过session获取的ServletContext然后获得输入流,读输入流
        ServletContext context = session.getServletContext();
        InputStream in = context.getResourceAsStream("/files/abc.txt");
        body = new byte[in.available()];
        in.read(body);

        // 设置HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=abc.txt");

        // 设置statusCode
        HttpStatus statusCode = HttpStatus.OK;
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, statusCode);
        return response;
    }

    /**
     * HttpMessageConverter实现类的测试方法,
     * 以及@ResponseBody和@RequestBody的示例
     * 这里实现了一个文件上传
     * 但是这个会把表单的内容和文件的内容一起上传,需要区分,没有实际的应用价值
     * 这里是入参和返回的都是字符串,一般入参用byte[]数组其实
     * 因而都会调用StringHttpMessageConverter
     * @return
     */
    @ResponseBody
    @RequestMapping("/testHttpMessageConverter")
//    public String testHttpMessageConverter (@RequestBody byte[] body) {
    public String testHttpMessageConverter (@RequestBody String body) {
        // body就是上传的文件的内容
        System.out.println(body);
        return "helloWorld" + new Date();
    }

    /**
     * 测试springMVC返回json的方法
     * @return
     */
    @ResponseBody
    @RequestMapping("/testJson")
    public Collection<Employee> testJson(){
        return employeeDao.getAll();
    }

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
