package br.com.postgreSQL.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.postgreSQL.dto.employee.EmployeeDto;
import br.com.postgreSQL.dto.employee.EmployeeDtoListWrapper;
import br.com.postgreSQL.service.employee.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class EmployeeController {

	private final EmployeeService employeeService;

	// Create employee.
	@PostMapping("/employee")
	public ResponseEntity<EmployeeDto> createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		EmployeeDto employeeCreate = employeeService.createEmployee(employeeDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(employeeCreate);
	}

	// Create list employee.
	@PostMapping("/employees")
	public ResponseEntity<List<EmployeeDto>> createEmployees(@RequestBody @Valid EmployeeDtoListWrapper wrapper) {
		List<EmployeeDto> employees = employeeService.createListEmployee(wrapper.getEmployees());
		return ResponseEntity.status(HttpStatus.CREATED).body(employees);
	}

	// Find employee by document.
	@GetMapping("/employee/{document}")
	public ResponseEntity<EmployeeDto> findEmployeeByDocument(@PathVariable String document) {
		EmployeeDto employeeByDocument = employeeService.findEmployeeByDocument(document);
		return ResponseEntity.ok().body(employeeByDocument);
	}

	// Find all employees.
	@GetMapping("/employees")
	public ResponseEntity<Page<EmployeeDto>> findAllEmployees(
			@PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.ok(employeeService.findAllEmployees(pageable));
	}

	// Delete employee by document.
	@DeleteMapping("/employee/{document}")
	public ResponseEntity<Void> deleteEmployeeByDocument(@PathVariable String document) {
		employeeService.deleteEmployeeByDocument(document);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	// Update employee by document.
	@PutMapping("/employee/{document}")
	public ResponseEntity<EmployeeDto> updateEmployeeByDocument(@PathVariable String document,
			@RequestBody @Valid EmployeeDto employeeDto) {
		EmployeeDto updateEmployee = employeeService.updateEmployeeByDocument(document, employeeDto);
		return ResponseEntity.ok().body(updateEmployee);
	}
}
