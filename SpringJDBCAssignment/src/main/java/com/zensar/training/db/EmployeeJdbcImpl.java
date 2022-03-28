package com.zensar.training.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.zensar.training.bean.Employee;
import com.zensar.training.bean.Gender;
import com.zensar.training.bean.JdbcConfig;
import com.zensar.training.service.EmployeeService;

@Repository
public class EmployeeJdbcImpl implements EmployeeDAO {

	DataSource datasource = new JdbcConfig().dataSource();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);

	@Override
	public boolean addEmployee(JdbcTemplate jdbcTemplate, Employee employee) throws Exception {
		
		
		jdbcTemplate=new JdbcTemplate(this.datasource);

		boolean result = false;
		Object[] rowData = { employee.getName(), employee.getHiredDate(), employee.getGrade(),
				employee.getBasicSalary(), employee.getGender().toString().charAt(0) };
		int r = jdbcTemplate.update(INSERT_QRY, rowData);
		if (r == 0)
			result = false;

		return result;
	}

	@Override
	public boolean updateEmployee(JdbcTemplate jdbcTemplate, Employee employee) throws Exception {
		boolean result = false;
		Object[] rowData = { employee.getName(), employee.getHiredDate(), employee.getGrade(),
				employee.getBasicSalary(), employee.getGender().toString().charAt(0) };
		int r = jdbcTemplate.update(UPDATE_QRY, rowData);
		if (r == 0)
			result = false;

		return result;
	}

	@Override
	public boolean deleteEmployee(JdbcTemplate jdbcTemplate, Employee employee) throws Exception {
		boolean result = false;
		Object[] data = { employee.getId() };

		int r = jdbcTemplate.update(DELETE_QRY, data);
		if (r == 0)
			result = false;
		return result;
	}

	@Override
	public Employee findEmployee(JdbcTemplate jdbcTemplate, int empId) throws Exception {

		Employee employee = jdbcTemplate.queryForObject(FIND_QRY, new Object[] { empId }, new EmployeeRowMapper());
		return employee;
	}

	@Override
	public List<Employee> findAllEmployees(JdbcTemplate jdbcTemplate) throws Exception {

		List<Employee> employees = jdbcTemplate.query(FIND_ALL_QRY, new EmployeeRowMapper());

		return employees;
	}

	private class EmployeeRowMapper implements RowMapper<Employee> {

		@Override
		public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
			Employee employee = new Employee();
			employee.setId(rs.getInt(1));
			employee.setName(rs.getString(2));

			java.sql.Date dojdate = rs.getDate(3);
			LocalDate doj = DateConversion.convertToLocalDate(dojdate);
			employee.setHiredDate(doj);
			employee.setGrade(rs.getString(4).charAt(0));
			employee.setBasicSalary(rs.getDouble(5));

			char gend = rs.getString(6).charAt(0);
			if (gend == 'M')
				employee.setGender(Gender.MALE);
			else
				employee.setGender(Gender.FEMALE);

			return employee;
		}

	}

}
