package br.com.postgreSQL.service.employee;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.postgreSQL.dto.employee.EmployeeDto;
import br.com.postgreSQL.dto.mapper.employee.EmployeeMapper;
import br.com.postgreSQL.model.Employee;
import br.com.postgreSQL.repository.EmployeeRepository;
import br.com.postgreSQL.service.validator.employee.EmployeeValidator;
import jakarta.transaction.Transactional;
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
	public List<EmployeeDto> createListEmployee(List<EmployeeDto> listDto) {
		
		List<Employee> employees = listDto.stream()
				.peek(dto -> employeeValidator.validateDocumentNotExists(dto.document()))
				.map(employeeMapper::toEntity)
				.collect(Collectors.toList());

		List<Employee> saved = employeeRepository.saveAll(employees);

		return saved.stream().map(employeeMapper::toDto).collect(Collectors.toList());
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

	@Transactional
	@Override
	public void deleteEmployeeByDocument(String document) {
		employeeValidator.validateDocumentExists(document);
		employeeRepository.deleteByDocument(document);
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
