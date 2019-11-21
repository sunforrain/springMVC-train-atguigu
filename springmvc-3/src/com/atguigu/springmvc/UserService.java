package com.atguigu.springmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 为了演示springMVC与spring整合
 * 以及是否会创建两个实例的问题,演示的Service
 */
@Service
public class UserService {

    // springIOC容器中的bean不能引用springMVC IOC容器中的bean,这里能发现是报错的
	@Autowired
	private HelloWorld helloWorld;
	
	public UserService() {
		System.out.println("UserService Constructor...");
	}
	
}
