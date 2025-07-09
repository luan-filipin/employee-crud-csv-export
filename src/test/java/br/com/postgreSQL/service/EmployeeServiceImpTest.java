package br.com.postgreSQL.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.postgreSQL.model.enums.EmployeeGender;
import br.com.postgreSQL.dto.EmployeeDto;
import br.com.postgreSQL.exception.DocumentAlreadyExistsException;
import br.com.postgreSQL.mapper.EmployeeMapper;
import br.com.postgreSQL.model.Address;
import br.com.postgreSQL.model.Employee;
import br.com.postgreSQL.repository.EmployeeRepository;
import br.com.postgreSQL.service.validator.EmployeeValidator;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImpTest {

	@Mock
	private EmployeeRepository employeeRepository;
	@Mock
	private EmployeeMapper employeeMapper;
	@Mock
	private EmployeeValidator employeeValidator;
	@InjectMocks
	private EmployeeServiceImp employeeServiceImp;

	@Test
	public void shouldCreateEmployeeSuccessfully() {

		Address address = new Address();
		address.setStreet("Rua A");
		address.setNumber(123L);
		address.setCity("Cidade X");
		address.setState("Estado Y");
		address.setZipCode("12345-678");
		address.setCountry("Brasil");

		EmployeeDto inputDto = new EmployeeDto("João Silva", "12345678900", LocalDate.of(1990, 1, 1),EmployeeGender.HOMEM, address);
		Employee entity = new Employee(1L, "João Silva", "12345678900", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
		Employee savedEntity = new Employee(1L, "João Silva", "12345678900", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
		EmployeeDto outputDto = new EmployeeDto("João Silva", "12345678900", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);

		doNothing().when(employeeValidator).validateDocumentNotExists("12345678900");
		when(employeeMapper.toEntity(inputDto)).thenReturn(entity);
		when(employeeRepository.save(entity)).thenReturn(savedEntity);
		when(employeeMapper.toDto(savedEntity)).thenReturn(outputDto);

		EmployeeDto result = employeeServiceImp.createEmployee(inputDto);

		assertNotNull(result);
		assertEquals(inputDto, result);
		assertEquals(inputDto.document(), result.document());

		verify(employeeValidator).validateDocumentNotExists("12345678900");
		verify(employeeRepository).save(entity);
		verify(employeeMapper).toDto(savedEntity);
		verify(employeeMapper).toEntity(inputDto);
	}

	@Test
	public void shouldThrowExceptionWhenDocumentAlreadyExists() {

		Address address = new Address();
		address.setStreet("Rua A");
		address.setNumber(123L);
		address.setCity("Cidade X");
		address.setState("Estado Y");
		address.setZipCode("12345-678");
		address.setCountry("Brasil");

		EmployeeDto inputDto = new EmployeeDto("João Silva", "12345678900", LocalDate.of(1990, 1, 1),
				EmployeeGender.HOMEM, address);

		// Simula que o documento já existe no sistema, forçando a falha do fluxo.
		doThrow(new DocumentAlreadyExistsException()).when(employeeValidator).validateDocumentNotExists("12345678900");

		// Verifica se o método createEmployee lança a exceção
		// DocumentAlreadyExistsException
		assertThrows(DocumentAlreadyExistsException.class, () -> {
			employeeServiceImp.createEmployee(inputDto);
		});

		// Garante que o método save do repositório nao foi chamado.
		verify(employeeRepository, never()).save(any());
	}

	@Test
	public void shouldCreateListEmployeesWithSuccessfully() {

		Address address = new Address();
		address.setStreet("Rua A");
		address.setNumber(123L);
		address.setCity("Cidade X");
		address.setState("Estado Y");
		address.setZipCode("12345-678");
		address.setCountry("Brasil");

		//Criando a lista de Dto para simular o recebimento dos dados.
		EmployeeDto employeeDto1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
		EmployeeDto employeeDto2 = new EmployeeDto("Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
		List<EmployeeDto> inputList = List.of(employeeDto1, employeeDto2);

		//Criando a lista de entity para simular a entrada e saida
		Employee entity1 = new Employee(null, "João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
		Employee entity2 = new Employee(null, "Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
		List<Employee> entityList = List.of(entity1, entity2);
		
		//Criando a lista de entity com os Ids.
	    Employee saved1 = new Employee(1L, "João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
	    Employee saved2 = new Employee(2L, "Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
	    List<Employee> savedList = List.of(saved1, saved2);
	    
	    // DTOs de saída esperados
	    EmployeeDto output1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
	    EmployeeDto output2 = new EmployeeDto("Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
	    List<EmployeeDto> expectedOutput = List.of(output1, output2);
	    
	    doNothing().when(employeeValidator).validateDocumentNotExists("11111111111");
	    doNothing().when(employeeValidator).validateDocumentNotExists("22222222222");
	    when(employeeMapper.toEntity(employeeDto1)).thenReturn(entity1);
	    when(employeeMapper.toEntity(employeeDto2)).thenReturn(entity2);
	    when(employeeRepository.saveAll(entityList)).thenReturn(savedList);
	    when(employeeMapper.toDto(saved1)).thenReturn(output1);
	    when(employeeMapper.toDto(saved2)).thenReturn(output2);
	    
	    List<EmployeeDto> result = employeeServiceImp.createListEmployee(inputList);
	    
	    assertNotNull(result);
	    assertEquals(2, result.size());
	    assertEquals(expectedOutput, result);
	    
	    
	    verify(employeeValidator).validateDocumentNotExists("11111111111");
	    verify(employeeValidator).validateDocumentNotExists("22222222222");
	    verify(employeeRepository).saveAll(entityList);
		
	}
	
	@Test
	public void shouldThrowException_whenAnyDocumentAlreadyExists() {
		
	    Address address = new Address();
	    address.setStreet("Rua A");
	    address.setNumber(123L);
	    address.setCity("Cidade X");
	    address.setState("Estado Y");
	    address.setZipCode("12345-678");
	    address.setCountry("Brasil");
	    
	    EmployeeDto employeeDto1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
	    EmployeeDto employeeDto2 = new EmployeeDto("Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
	    List<EmployeeDto> employeeList = List.of(employeeDto1, employeeDto2);
	    
	    doNothing().when(employeeValidator).validateDocumentNotExists("11111111111");
	    doThrow(new DocumentAlreadyExistsException()).when(employeeValidator).validateDocumentNotExists("22222222222");
	    
	    assertThrows(DocumentAlreadyExistsException.class, () -> {
	    	employeeServiceImp.createListEmployee(employeeList);
	    });
	    
	    verify(employeeRepository, never()).saveAll(any());
	    
	    
	}
}
