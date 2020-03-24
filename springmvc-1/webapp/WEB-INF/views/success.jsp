<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	
	<h4>Sucess Page</h4>

	<%--任务14：SpringMVC_处理模型数据之ModelAndView--%>
	time: ${requestScope.time }
	<br><br>
	<%--任务15：SpringMVC_处理模型数据之Map--%>
	names: ${requestScope.names }
	<br><br>
	<%--任务16：SpringMVC_处理模型数据之SessionAttributes注解,requestScope--%>
	request user: ${requestScope.user }
	<br><br>
	<%--任务16：SpringMVC_处理模型数据之SessionAttributes注解,sessionScope--%>
	session user: ${sessionScope.user }
	<br><br>
	<%--任务16：SpringMVC_处理模型数据之SessionAttributes注解,指定type类型--%>
	request school: ${requestScope.school }
	<br><br>
	<%--任务16：SpringMVC_处理模型数据之SessionAttributes注解,指定type类型--%>
	session school: ${sessionScope.school }
	<br><br>

	abc user: ${requestScope.abc }
	<br><br>

	mnxyz user: ${requestScope.mnxyz }
	<br><br>

	<%-- 别忘了先导入fmt标签和配置国际化文件 --%>
	<fmt:message key="i18n.username"></fmt:message>
	<br><br>

	<fmt:message key="i18n.password"></fmt:message>
	<br><br>
</body>
</html>