package br.com.postgreSQL.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.postgreSQL.dto.EmployeeDto;

public interface EmployeeService {

	//Create the employee
	EmployeeDto createEmployee(EmployeeDto employeeDto);
	
	//Returns specific employee by document number.
	EmployeeDto findEmployeeByDocument(String document);
	
	//Returns all employees.
	Page<EmployeeDto> findAllEmployees(Pageable pageable);
	
	//Delete employee by document number.
	void deleteEmployeeByDocument(String document);
	
	//Update employee by document number.
	EmployeeDto updateEmployeeByDocument(String document, EmployeeDto employDto);
	
}
