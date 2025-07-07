package br.com.postgreSQL.controller;

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

import br.com.postgreSQL.dto.EmployeeDto;
import br.com.postgreSQL.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api")
public class EmployeeController {
	
	private final EmployeeService employeeService;

	@PostMapping("/employee")
	public ResponseEntity<EmployeeDto> createEmployee(@RequestBody @Valid EmployeeDto employeeDto){
		EmployeeDto employeeCreate = employeeService.createEmployee(employeeDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(employeeCreate);
	}
	
	@GetMapping("/employee/{document}")
	public ResponseEntity<EmployeeDto> findEmployeeByDocument(@PathVariable String document){
		EmployeeDto employeeByDocument = employeeService.findEmployeeByDocument(document);
		return ResponseEntity.ok().body(employeeByDocument);
	}
	
	@GetMapping("/employee")
	public ResponseEntity<Page<EmployeeDto>> findAllEmployees(
			@PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
		return ResponseEntity.ok(employeeService.findAllEmployees(pageable));
	}
	
	@DeleteMapping("/employee/{document}")
	public ResponseEntity<Void> deleteEmployeeByDocument(@PathVariable String document){
		employeeService.deleteEmployeeByDocument(document);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@PutMapping("/employee/{document}")
	public ResponseEntity<EmployeeDto> updateEmployeeByDocument(@PathVariable String document, @RequestBody @Valid EmployeeDto employeeDto){
		EmployeeDto updateEmployee = employeeService.updateEmployeeByDocument(document, employeeDto);
		return ResponseEntity.ok().body(updateEmployee);
	}
	
}





