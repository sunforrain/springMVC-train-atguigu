<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
    // 用于展示json字符串返回值的方法,实际上就是在循环alert
	$(function(){
		$("#testJson").click(function(){
			var url = this.href;
			var args = {};
			$.post(url, args, function(data){
				for(var i = 0; i < data.length; i++){
					var id = data[i].id;
					var lastName = data[i].lastName;

					alert(id + ": " + lastName);
				}
			});
			return false;
		});
	})
</script>
</head>
<body>

	<%-- 测试文件上传的表单
	    这个是正经的上传--%>
	<form action="testFileUpload" method="POST" enctype="multipart/form-data">
		File: <input type="file" name="file"/>
		Desc: <input type="text" name="desc"/>
		<input type="submit" value="Submit"/>
	</form>

	<br><br>

	<a href="emps">List All Employees</a>
	<br><br>

	<a href="testJson" id="testJson">Test Json</a>
	<br><br>

    <%-- testHttpMessageConverter, 搞一个文件上传的效果
        但是这个会把表单的内容和文件的内容一起上传,需要区分,没有实际的应用价值 --%>
	<form action="testHttpMessageConverter" method="POST" enctype="multipart/form-data">
		File: <input type="file" name="file"/>
		Desc: <input type="text" name="desc"/>
		<input type="submit" value="Submit"/>
	</form>

	<br><br>

	<a href="testResponseEntity">Test ResponseEntity</a>

	<!--
		关于国际化:
		1. 在页面上能够根据浏览器语言设置的情况对文本(不是内容), 时间, 数值进行本地化处理
		2. 可以在 bean 中获取国际化资源文件 Locale 对应的消息
		3. 可以通过超链接切换 Locale, 而不再依赖于浏览器的语言设置情况

		解决:
		1. 使用 JSTL 的 fmt 标签(见i18n.jsp和i18n2.jsp的写法)
		2. 在 bean 中注入 ResourceBundleMessageSource 的实例, 使用其对应的 getMessage 方法即可
		    (见testI18n()方法)
		3. 配置 LocalResolver 和 LocaleChangeInterceptor
	-->
	<br><br>
	<a href="i18n">I18N PAGE</a>

	<br><br>
	<%-- 测试异常处理_ExceptionHandler注解相关, 制造一个除数为0的异常 --%>
	<a href="testExceptionHandlerExceptionResolver?i=0">Test ExceptionHandlerExceptionResolver</a>

	<br><br>
	<%--<a href="testResponseStatusExceptionResolver?i=10">Test ResponseStatusExceptionResolver</a>--%>
	<%----%>
	<%--<br><br>--%>
	<%--<a href="testDefaultHandlerExceptionResolver">Test DefaultHandlerExceptionResolver</a>--%>
	
	<br><br>
	<a href="testSimpleMappingExceptionResolver?i=2">Test SimpleMappingExceptionResolver</a>

</body>
</html>