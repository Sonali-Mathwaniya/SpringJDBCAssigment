package com.zensar.training.db;

import java.sql.Connection;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.zensar.training.bean.Employee;

public interface EmployeeDAO {
	String INSERT_QRY="insert into TEMPLOYEES(NAME,DOJ,GRADE,BASIC_SALARY,GENDER) values(?,?,?,?,?)";
	String UPDATE_QRY="update TEMPLOYEES set NAME=?,DOJ=?,GRADE=?,BASIC_SALARY=?, GENDER=? where ID=?";
	String DELETE_QRY="delete from TEMPLOYEES where ID=?";
	String FIND_QRY="select * from TEMPLOYEES where ID=?";
	String FIND_ALL_QRY="select * from TEMPLOYEES";
	
	boolean addEmployee(JdbcTemplate jdbctemplate, Employee employee) throws Exception;
	boolean updateEmployee(JdbcTemplate jdbctemplate, Employee employee) throws Exception;
	boolean deleteEmployee(JdbcTemplate jdbctemplate, Employee employee) throws Exception;
	Employee findEmployee(JdbcTemplate jdbctemplate, int empId) throws Exception;
	List<Employee> findAllEmployees(JdbcTemplate jdbctemplate) throws Exception;
	
}
