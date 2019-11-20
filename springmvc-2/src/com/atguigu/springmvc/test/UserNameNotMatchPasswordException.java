package com.atguigu.springmvc.test;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 自定义一个异常,用于演示@ResponseStatus注解
 * 修饰类的时候,会在弹出对应异常时返回异常类修饰的错误码
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "用户名和密码不匹配!")
public class UserNameNotMatchPasswordException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
}
