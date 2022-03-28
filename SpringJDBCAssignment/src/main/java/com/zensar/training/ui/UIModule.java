package com.zensar.training.ui;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zensar.training.bean.Employee;
import com.zensar.training.bean.Gender;
import com.zensar.training.db.DBConnectionFactory;
import com.zensar.training.db.EmployeeDAO;
import com.zensar.training.db.EmployeeJdbcImpl;
import com.zensar.training.service.EmployeeService;
import com.zensar.training.service.EmployeeServiceImpl;

public class UIModule {
	
	private static DataSource datasource;
	private static JdbcTemplate jdbcTemplate;
	
	@Qualifier("datasource")
	public void setDataSource(DataSource dataSource) {
		this.datasource=dataSource;
	}
	
	private static void blankLines(int num) {
		for (int i = 1; i <= num; i++)
			System.out.println();
	}

	public static void addInfo() {
		blankLines(3);
		InputPrompter prompter = new InputPrompter();
		//JdbcTemplate jdbcTemplate=new JdbcTemplate(datasource);

		String name = prompter.promptForStringInput("Enter Name");
		char grade = prompter.promptForCharInput("Enter Grade[A,B,C]");
		LocalDate hiredDate = prompter.promptForDateInput("Enter DOJ", "dd-MMM-yyyy");
		double salary = prompter.promptForDoubleInput("Enter Basic Salary");
		Gender gender = prompter.promptForGenderInput("Enter Gender [1.MALE 2.FEMALE]");

		// more code here
		Employee emp = new Employee();
		emp.setName(name);
		emp.setGrade(grade);
		emp.setHiredDate(hiredDate);
		emp.setBasicSalary(salary);
		emp.setGender(gender);

		EmployeeService empService = new EmployeeServiceImpl();
		try {
			boolean result = empService.addEmployee(jdbcTemplate,emp);
			if (result == true)
				System.out.println("\t\t Added Successfully");
			else
				System.out.println("Not Added");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateInfo() {
		blankLines(3);
		InputPrompter prompter = new InputPrompter();
		Employee employee = new Employee();
		//JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);

		int editableID = prompter.promptForIntInput("Enter ID to Update");
		EmployeeService empService = new EmployeeServiceImpl();
		try {
			employee = empService.findEmployee(jdbcTemplate,editableID);
			if (employee == null) {
				System.out.println("Employee Id does not exists");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String name = prompter.promptForStringInput("Enter Name");
		char grade = prompter.promptForCharInput("Enter Grade [A,B,C]");
		LocalDate hiredDate = prompter.promptForDateInput("Enter DOJ", "dd-MMM-yyyy");
		double salary = prompter.promptForDoubleInput("Enter Basic Salary");
		Gender gender = prompter.promptForGenderInput("Enter Gender [1.MALE 2.FEMALE]");

		employee.setId(editableID);
		employee.setName(name);
		employee.setGrade(grade);
		employee.setHiredDate(hiredDate);
		employee.setBasicSalary(salary);
		employee.setGender(gender);

		try {
			boolean result = empService.updateEmployee(jdbcTemplate,employee);
			if (result == true)
				System.out.println("\t\t Updated Successfully");
			else
				System.out.println("Not Updated");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void searchInfo() {
		blankLines(3);
		InputPrompter prompter = new InputPrompter();
		//JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
		int searchId = prompter.promptForIntInput("Enter ID to Search");

		Consumer<Employee> consumer = (e) -> {
			System.out.println("\t\t ID: " + e.getId());
			System.out.println("\t\t Name: " + e.getName());
			System.out.println("\t\t DOJ: " + e.getHiredDate());
			System.out.println("\t\t SALARY: " + e.getBasicSalary());
			System.out.println("\t\t GRADE: " + e.getGrade());
			System.out.println("\t\t GENDER: " + e.getGender());
		};

		EmployeeService empService = new EmployeeServiceImpl();
		try {
			Employee employee = empService.findEmployee(jdbcTemplate,searchId);
			if (employee == null) {
				System.out.println("Employee not found");
				return;
			}
			consumer.accept(employee);

		} catch (Exception e) {

		}

	}

	public static void listInfo() {
		blankLines(3);
		List<Employee> employees = new ArrayList<>();
		//JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
		EmployeeService empService = new EmployeeServiceImpl();
		Consumer<Employee> consumer = (e) -> {
			System.out.printf("%-5d", e.getId());
			System.out.printf("%-25s", e.getName());
			System.out.printf("%-15s", e.getHiredDate().toString());
			System.out.printf("%-5s", e.getGrade() + "");
			System.out.printf("%-15.2f", e.getBasicSalary());
			System.out.printf("%-5s", e.getGender().toString());
			System.out.println();
		};
		try {
			employees = empService.findAllEmployees(jdbcTemplate);

		} catch (Exception e2) {

		}
		employees.stream().forEach(consumer);
	}

	public static void deleteInfo() {
		blankLines(3);
		InputPrompter prompter = new InputPrompter();
		//JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
		int searchId = prompter.promptForIntInput("Enter ID to Delete");
		boolean result = false;
		EmployeeService empService = new EmployeeServiceImpl();
		try {
			result = empService.deleteEmployee(jdbcTemplate,new Employee(searchId));

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (result == true)
			System.out.println("\t\t Deleted  Successfully");
		else
			System.out.println("\t\t Not Deleted");
	}
}