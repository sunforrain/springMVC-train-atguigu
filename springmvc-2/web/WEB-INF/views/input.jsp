<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%-- springMVC的表单标签,下面有使用标签的说明 --%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>

<%-- 自定义一个转换器,直接将一个字符串转换成一个employee对象,字符串格式如下 --%>
<form action="${pageContext.request.contextPath }/testConversionServiceConverer" method="POST">
    <!-- lastname-email-gender-department.id 例如: GG-gg@atguigu.com-0-105 -->
    Employee: <input type="text" name="employee"/>
    <input type="submit" value="Submit"/>
</form>
<br><br>

<!--
    1. WHY 使用 form 标签呢 ?
    可以更快速的开发出表单页面, 而且可以更方便的进行表单值的回显
    2. 注意:
    可以通过 modelAttribute 属性指定绑定的模型属性,
    若没有指定该属性，则默认从 request 域对象中读取 command 的表单 bean
    如果该属性值也不存在，则会发生错误。
-->
<%-- 这里如果使用相对路径 action = "emp", 修改操作路径会变成提交给 http://localhost:8080/emp/emp 实际开发建议用绝对路径 --%>
<form:form action="${pageContext.request.contextPath }/emp" method="POST" modelAttribute="employee">
    <%-- 因为保存和更改复用一个页面,需要在这里作区分 --%>
    <c:if test="${employee.id == null}">
        <!-- path 属性对应 html 表单标签的 name 属性值 -->
        LastName: <form:input path="lastName"/>
    </c:if>
    <c:if test="${employee.id != null}">
        <!-- 要求修改时不能更改lastName,传id就可以了 -->
        <form:hidden path="id"/>
        <%-- 更新需要改为PUT请求 --%>
        <%-- 对于 _method 不能使用 form:hidden 标签, 因为 modelAttribute 对应的 bean 中没有 _method 这个属性 --%>
        <input type="hidden" name="_method" value="PUT">
    </c:if>
    <br>
    Email: <form:input path="email"/>
    <br>
    <%
        Map<String, String> genders = new HashMap();
        genders.put("1", "Male");
        genders.put("0", "Female");
        request.setAttribute("genders", genders);
    %>
    <%-- delimiter：多个单选框可以通过 delimiter – 指定分隔符 --%>
    Gender:
    <br>
    <form:radiobuttons path="gender" items="${genders }" delimiter="<br>"/>
    <br>
    <%-- path 属性支持级联 --%>
    <%--
         items：可以是一个 List、String[] 或 Map
         itemValue：指定 radio 的 value 值。可以是集合中 bean 的一个属性值
         itemLabel：指定 radio 的 label – 值
     --%>
    Department: <form:select path="department.id"
                             items="${departments }" itemLabel="departmentName" itemValue="id"></form:select>
    <br>
    <%--
        1. 数据类型转换
        2. 数据类型格式化
        3. 数据校验.
    --%>
    Birth: <form:input path="birth"/>
    <br>
    Salary: <form:input path="salary"/>
    <br>
    <input type="submit" value="Submit"/>
</form:form>
<%--<br><br>--%>
<%--<form:form action="${pageContext.request.contextPath }/emp" method="POST"--%>
<%--modelAttribute="employee">--%>

<%--<form:errors path="*"></form:errors>--%>
<%--<br>--%>

<%--<c:if test="${employee.id == null }">--%>
<%--<!-- path 属性对应 html 表单标签的 name 属性值 -->--%>
<%--LastName: <form:input path="lastName"/>--%>
<%--<form:errors path="lastName"></form:errors>--%>
<%--</c:if>--%>
<%--<c:if test="${employee.id != null }">--%>
<%--<form:hidden path="id"/>--%>
<%--<input type="hidden" name="_method" value="PUT"/>--%>
 <%--对于 _method 不能使用 form:hidden 标签, 因为 modelAttribute 对应的 bean 中没有 _method 这个属性 --%>
<%--&lt;%&ndash;--%>
<%--<form:hidden path="_method" value="PUT"/>--%>
<%--&ndash;%&gt;--%>
<%--</c:if>--%>

<%--<br>--%>
<%--Email: <form:input path="email"/>--%>
<%--<form:errors path="email"></form:errors>--%>
<%--<br>--%>
<%--<%--%>
<%--Map<String, String> genders = new HashMap();--%>
<%--genders.put("1", "Male");--%>
<%--genders.put("0", "Female");--%>

<%--request.setAttribute("genders", genders);--%>
<%--%>--%>
<%--Gender:--%>
<%--<br>--%>
<%--<form:radiobuttons path="gender" items="${genders }" delimiter="<br>"/>--%>
<%--<br>--%>
<%--Department: <form:select path="department.id"--%>
<%--items="${departments }" itemLabel="departmentName" itemValue="id"></form:select>--%>
<%--<br>--%>
<%--<!----%>
<%--1. 数据类型转换--%>
<%--2. 数据类型格式化--%>
<%--3. 数据校验.--%>
<%--1). 如何校验 ? 注解 ?--%>
<%--①. 使用 JSR 303 验证标准--%>
<%--②. 加入 hibernate validator 验证框架的 jar 包--%>
<%--③. 在 SpringMVC 配置文件中添加 <mvc:annotation-driven />--%>
<%--④. 需要在 bean 的属性上添加对应的注解--%>
<%--⑤. 在目标方法 bean 类型的前面添加 @Valid 注解--%>
<%--2). 验证出错转向到哪一个页面 ?--%>
<%--注意: 需校验的 Bean 对象和其绑定结果对象或错误对象时成对出现的，它们之间不允许声明其他的入参--%>
<%--3). 错误消息 ? 如何显示, 如何把错误消息进行国际化--%>
<%---->--%>
<%--Birth: <form:input path="birth"/>--%>
<%--<form:errors path="birth"></form:errors>--%>
<%--<br>--%>
<%--Salary: <form:input path="salary"/>--%>
<br>
<%--<input type="submit" value="Submit"/>--%>
<%--</form:form>--%>

</body>
</html>