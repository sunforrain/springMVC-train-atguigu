package com.atguigu.springmvc.test;

import com.atguigu.springmvc.crud.dao.EmployeeDao;
import com.atguigu.springmvc.crud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
