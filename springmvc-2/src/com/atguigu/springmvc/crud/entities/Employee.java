package com.atguigu.springmvc.crud.entities;

import java.util.Date;

import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

// 任务30：SpringMVC_RESTRUL_CRUD_显示所有员工信息
public class Employee {

	private Integer id;
	// JSR303验证用的注解, 非空
	@NotEmpty
	private String lastName;

    // JSR303验证用的注解, 邮件格式
	@Email
	private String email;
	//1 male, 0 female
	private Integer gender;
	
	private Department department;

    // JSR303验证用的注解, 今日之前的时间
	@Past
	// @DateTimeFormat用于日期格式的转换,yyyy-MM-dd格式的字符串自动转为DATE类型
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birth;

	// @NumberFormat用于数值类型转换, #代表数字, 将#,###,###.#格式的字符串转为Float类型
	@NumberFormat(pattern="#,###,###.#")
	private Float salary;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Float getSalary() {
		return salary;
	}

	public void setSalary(Float salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", lastName=" + lastName + ", email="
				+ email + ", gender=" + gender + ", department=" + department
				+ ", birth=" + birth + ", salary=" + salary + "]";
	}

	public Employee(Integer id, String lastName, String email, Integer gender,
			Department department) {
		super();
		this.id = id;
		this.lastName = lastName;
		this.email = email;
		this.gender = gender;
		this.department = department;
	}

	public Employee() {
		// TODO Auto-generated constructor stub
	}
}
