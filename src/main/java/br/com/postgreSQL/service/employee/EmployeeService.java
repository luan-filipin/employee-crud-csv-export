package br.com.postgreSQL.service.employee;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.postgreSQL.dto.employee.EmployeeDto;

public interface EmployeeService {

	//Create the employee.
	EmployeeDto createEmployee(EmployeeDto employeeDto);
	
	//Create a list of employees.
	List<EmployeeDto> createListEmployee(List<EmployeeDto> listDto);
	
	//Returns specific employee by document number.
	EmployeeDto findEmployeeByDocument(String document);
	
	//Returns all employees.
	Page<EmployeeDto> findAllEmployees(Pageable pageable);
	
	//Delete employee by document number.
	void deleteEmployeeByDocument(String document);
	
	//Update employee by document number.
	EmployeeDto updateEmployeeByDocument(String document, EmployeeDto employDto);
	
}
