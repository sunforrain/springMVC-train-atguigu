<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>

<a href="springmvc/testRedirect">Test Redirect</a>
<br><br>

<a href="springmvc/testView">Test View</a>
<br><br>

<a href="springmvc/testViewAndViewResolver">Test ViewAndViewResolver</a>
<br><br>

<!--
    任务18：SpringMVC_ModelAttribute注解之示例代码
    模拟修改操作
    1. 原始数据为: 1, Tom, 123456,tom@atguigu.com,12
    2. 密码不能被修改.
    3. 表单回显, 模拟操作直接在表单填写对应的属性值
-->
<form action="springmvc/testModelAttribute" method="Post">
    <input type="hidden" name="id" value="1"/>
    username: <input type="text" name="username" value="Tom"/>
    <br>
    email: <input type="text" name="email" value="tom@atguigu.com"/>
    <br>
    age: <input type="text" name="age" value="12"/>
    <br>
    <input type="submit" value="Submit"/>
</form>
<br><br>

<%--任务16：SpringMVC_处理模型数据之SessionAttributes注解--%>
<a href="springmvc/testSessionAttributes">Test SessionAttributes</a>
<br><br>

<%--任务15：SpringMVC_处理模型数据之Map--%>
<a href="springmvc/testMap">Test Map</a>
<br><br>

<%--任务14：SpringMVC_处理模型数据之ModelAndView--%>
<a href="springmvc/testModelAndView">Test ModelAndView</a>
<br><br>

<%--任务13：SpringMVC_使用Servlet原生API作为参数--%>
<a href="springmvc/testServletAPI">Test ServletAPI</a>
<br><br>

<%--任务12：SpringMVC_使用POJO作为参数--%>
<form action="springmvc/testPojo" method="post">
    username: <input type="text" name="username"/>
    <br>
    password: <input type="password" name="password"/>
    <br>
    email: <input type="text" name="email"/>
    <br>
    age: <input type="text" name="age"/>
    <br>
        <%--属性的属性可以用.级联--%>
    city: <input type="text" name="address.city"/>
    <br>
    province: <input type="text" name="address.province"/>
    <br>
    <input type="submit" value="Submit"/>
</form>
<br><br>

<%--任务11：SpringMVC_CookieValue注解--%>
<a href="springmvc/testCookieValue">Test CookieValue</a>
<br><br>

<%--任务10：SpringMVC_RequestHeader注解--%>
<a href="springmvc/testRequestHeader">Test RequestHeader</a>
<br><br>

<%--任务9：SpringMVC_RequestParam注解--%>
<a href="springmvc/testRequestParam?username=atguigu&age=11">Test RequestParam</a>
<br><br>

<!--任务8：SpringMVC_RequestMapping_HiddenHttpMethodFilter过滤器, PUT请求-->
<form action="springmvc/testRest/1" method="post">
    <input type="hidden" name="_method" value="PUT"/>
    <input type="submit" value="TestRest PUT"/>
</form>
<br><br>

<!--任务8：SpringMVC_RequestMapping_HiddenHttpMethodFilter过滤器, DELETE请求-->
<form action="springmvc/testRest/1" method="post">
    <input type="hidden" name="_method" value="DELETE"/>
    <input type="submit" value="TestRest DELETE"/>
</form>
<br><br>

<!--任务8：SpringMVC_RequestMapping_HiddenHttpMethodFilter过滤器, POST请求-->
<form action="springmvc/testRest" method="post">
    <input type="submit" value="TestRest POST"/>
</form>
<br><br>

<!--任务8：SpringMVC_RequestMapping_HiddenHttpMethodFilter过滤器, GET请求-->
<a href="springmvc/testRest/1">Test Rest Get</a>
<br><br>

<%--任务7：SpringMVC_RequestMapping_PathVariable注解--%>
<a href="springmvc/testPathVariable/1">Test PathVariable</a>
<br><br>

<%--任务6：SpringMVC_RequestMapping_Ant路径--%>
<a href="springmvc/testAntPath/mnxyz/abc">Test AntPath</a>
<br><br>

<%--任务5：SpringMVC_RequestMapping_请求参数&请求头--%>
<a href="springmvc/testParamsAndHeaders?username=atguigu&age=10">Test ParamsAndHeaders</a>
<br><br>

<%--任务4：SpringMVC_RequestMapping_请求方式, 指定请求方法为post--%>
<form action="springmvc/testMethod" method="POST">
    <input type="submit" value="submit"/>
</form>
<br><br>

<%--任务4：SpringMVC_RequestMapping_请求方式, 普通的请求是get请求,会报405错误--%>
<a href="springmvc/testMethod">Test Method</a>
<br><br>

<%--任务3：SpringMVC_RequestMapping_修饰类--%>
<a href="springmvc/testRequestMapping">Test RequestMapping</a>
<br><br>

<a href="helloworld">Hello World</a>

</body>
</html>