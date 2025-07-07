package br.com.postgreSQL.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.postgreSQL.dto.EmployeeDto;
import br.com.postgreSQL.mapper.EmployeeMapper;
import br.com.postgreSQL.model.Employee;
import br.com.postgreSQL.repository.EmployeeRepository;
import br.com.postgreSQL.service.validator.EmployeeValidator;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImp implements EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;
	private final EmployeeValidator employeeValidator;

	@Override
	public EmployeeDto createEmployee(EmployeeDto employeeDto) {
		employeeValidator.validateDocumentNotExists(employeeDto.document());
		Employee employee = employeeMapper.toEntity(employeeDto);
		Employee employeeSalved = employeeRepository.save(employee);
		return employeeMapper.toDto(employeeSalved);
	}

	@Override
	public EmployeeDto findEmployeeByDocument(String document) {
		employeeValidator.validateDocumentExists(document);
		Employee employee = employeeRepository.findByDocument(document);
		return employeeMapper.toDto(employee);
	}

	@Override
	public Page<EmployeeDto> findAllEmployees(Pageable pageable) {
		Page<Employee> employeePage = employeeRepository.findAll(pageable);
		return employeePage.map(employeeMapper::toDto);
	}

	@Override
	public void deleteEmployeeByDocument(String document) {
		employeeValidator.validateDocumentExists(document);
		employeeRepository.deleteAllByDocument(document);
	}

	@Override
	public EmployeeDto updateEmployeeByDocument(String document, EmployeeDto employeeDto) {
		employeeValidator.validateDocumentExists(document);
		Employee employee = employeeRepository.findByDocument(document);
		employeeValidator.validateDocumentImmutable(employee.getDocument(), employeeDto.document());
		employeeMapper.updateFromDto(employeeDto, employee);
		Employee updateEmployee = employeeRepository.save(employee);
		return employeeMapper.toDto(updateEmployee);
	}

}
