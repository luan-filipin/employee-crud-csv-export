package br.com.postgreSQL.service.employee;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.postgreSQL.model.enums.EmployeeGender;
import br.com.postgreSQL.dto.employee.AddressDto;
import br.com.postgreSQL.dto.employee.EmployeeDto;
import br.com.postgreSQL.dto.mapper.employee.EmployeeMapper;
import br.com.postgreSQL.exception.employee.DocumentAlreadyExistsException;
import br.com.postgreSQL.exception.employee.DocumentImmutableException;
import br.com.postgreSQL.exception.employee.DocumentNotFoundException;
import br.com.postgreSQL.model.Address;
import br.com.postgreSQL.model.Employee;
import br.com.postgreSQL.repository.EmployeeRepository;
import br.com.postgreSQL.service.employee.EmployeeServiceImp;
import br.com.postgreSQL.service.validator.employee.EmployeeValidator;

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
		
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
		EmployeeDto inputDto = new EmployeeDto("João Silva", "12345678900", LocalDate.of(1990, 1, 1),EmployeeGender.HOMEM, addressDto);
		Employee entity = new Employee(1L, "João Silva", "12345678900", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
		Employee savedEntity = new Employee(1L, "João Silva", "12345678900", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
		EmployeeDto outputDto = new EmployeeDto("João Silva", "12345678900", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);

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
	public void shouldThrowException_whenDocumentAlreadyExists() {
		
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
		EmployeeDto inputDto = new EmployeeDto("João Silva", "12345678900", LocalDate.of(1990, 1, 1),
				EmployeeGender.HOMEM, addressDto);

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
		
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
		//Criando a lista de Dto para simular o recebimento dos dados.
		EmployeeDto employeeDto1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);
		EmployeeDto employeeDto2 = new EmployeeDto("Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, addressDto);
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
	    EmployeeDto output1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);
	    EmployeeDto output2 = new EmployeeDto("Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, addressDto);
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
		
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
	    EmployeeDto employeeDto1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);
	    EmployeeDto employeeDto2 = new EmployeeDto("Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, addressDto);
	    List<EmployeeDto> employeeList = List.of(employeeDto1, employeeDto2);
	    
	    doNothing().when(employeeValidator).validateDocumentNotExists("11111111111");
	    doThrow(new DocumentAlreadyExistsException()).when(employeeValidator).validateDocumentNotExists("22222222222");
	    
	    assertThrows(DocumentAlreadyExistsException.class, () -> {
	    	employeeServiceImp.createListEmployee(employeeList);
	    });
	    
	    verify(employeeRepository, never()).saveAll(any());  
	}
	
	
	
	@Test
	public void shouldFindEmployeeByDocumentWithSuccessfully() {
		
		String document = "11111111111";
		
	    Address address = new Address();
	    address.setStreet("Rua A");
	    address.setNumber(123L);
	    address.setCity("Cidade X");
	    address.setState("Estado Y");
	    address.setZipCode("12345-678");
	    address.setCountry("Brasil");
	    
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
	    Employee employee = new Employee(1L, "João Silva", document, LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
	    EmployeeDto expectedDto = new EmployeeDto("João Silva", document, LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);
		
		doNothing().when(employeeValidator).validateDocumentExists(document);
		when(employeeRepository.findByDocument(document)).thenReturn(employee);
		when(employeeMapper.toDto(employee)).thenReturn(expectedDto);
		
		EmployeeDto result = employeeServiceImp.findEmployeeByDocument(document);
		
		assertNotNull(result);
		assertEquals(expectedDto, result);
		assertEquals("João Silva", result.name());
		assertEquals(document, result.document());
		
		verify(employeeValidator).validateDocumentExists(document);
		verify(employeeRepository).findByDocument(document);
		verify(employeeMapper).toDto(employee);
		
	}
	
	@Test
	public void shouldThrowException_whenDocumentDoesNotExist() {
		String document = "000000000000";
		
		doThrow(new DocumentNotFoundException()).when(employeeValidator).validateDocumentExists(document);
		
		assertThrows(DocumentNotFoundException.class, () -> {
			employeeServiceImp.findEmployeeByDocument(document);
		});
		
		verify(employeeRepository, never()).findByDocument(any());
		verify(employeeMapper, never()).toDto(any());
	}
	
	@Test
	public void shouldReturnPagedEmployeesSuccessfully() {
		
	    Pageable pageable = PageRequest.of(0, 2);

	    Address address = new Address();
	    address.setStreet("Rua A");
	    address.setNumber(123L);
	    address.setCity("Cidade X");
	    address.setState("Estado Y");
	    address.setZipCode("12345-678");
	    address.setCountry("Brasil");
	    
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");

	    Employee employee1 = new Employee(1L, "João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
	    Employee employee2 = new Employee(1L, "Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
	    
	    List<Employee> employeeList = List.of(employee1, employee2);
	    Page<Employee> employeePage = new PageImpl<>(employeeList);
	    
	    EmployeeDto dto1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);
	    EmployeeDto dto2 = new EmployeeDto("Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, addressDto);

		when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
		when(employeeMapper.toDto(employee1)).thenReturn(dto1);
		when(employeeMapper.toDto(employee2)).thenReturn(dto2);
		
		Page<EmployeeDto> result = employeeServiceImp.findAllEmployees(pageable);
		
		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		assertEquals("João Silva", result.getContent().get(0).name());
		assertEquals("Maria Lima", result.getContent().get(1).name());
		
		verify(employeeRepository).findAll(pageable);
		verify(employeeMapper).toDto(employee1);
		verify(employeeMapper).toDto(employee2);
	    
	}
	
	@Test
	public void shouldThrowException_whenRepositoryFails() {
		
		Pageable pageable = PageRequest.of(0, 2);
		
		when(employeeRepository.findAll(pageable)).thenThrow(new RuntimeException("Error ao acessar o banco"));
		
		RuntimeException exception = assertThrows(RuntimeException.class, ()->{
			employeeServiceImp.findAllEmployees(pageable);
		});
		
		assertEquals("Error ao acessar o banco", exception.getMessage());
		
		verify(employeeMapper, never()).toDto(any());
	}
	
	@Test
	public void shouldDeleteEmployeeByDocumentWithSuccessfully() {
		
		String document = "11111111111";
		
		doNothing().when(employeeValidator).validateDocumentExists(document);
		doNothing().when(employeeRepository).deleteByDocument(document);
		
		employeeServiceImp.deleteEmployeeByDocument(document);
		
		verify(employeeValidator).validateDocumentExists(document);
		verify(employeeRepository).deleteByDocument(document);
		
	}
	
	@Test
	public void shouldThrowException_whenDocumentDoesNotExist_onDelete() {
		
		String document = "000000000000";
		
		doThrow(new DocumentNotFoundException()).when(employeeValidator).validateDocumentExists(document);
		
		assertThrows(DocumentNotFoundException.class, ()->{
			employeeServiceImp.deleteEmployeeByDocument(document);
		});
		
		verify(employeeRepository, never()).deleteByDocument(any());
		
		
	}

	@Test
	public void shouldUpdateEmployeeByDocumentWithSuccessfully() {
		
		String document = "11111111111";
		
	    Address address = new Address();
	    address.setStreet("Rua A");
	    address.setNumber(123L);
	    address.setCity("Cidade X");
	    address.setState("Estado Y");
	    address.setZipCode("12345-678");
	    address.setCountry("Brasil");
	    
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
	    
	    EmployeeDto inputDto = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);
	    Employee entity = new Employee(null, "João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
	    Employee salvedEntity = new Employee(1L, "João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
	    EmployeeDto output = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);
	    
	    doNothing().when(employeeValidator).validateDocumentExists(document);
	    when(employeeRepository.findByDocument(document)).thenReturn(entity);
	    doNothing().when(employeeValidator).validateDocumentImmutable(document, inputDto.document());
	    doNothing().when(employeeMapper).updateFromDto(inputDto, entity);
	    when(employeeRepository.save(entity)).thenReturn(salvedEntity);
	    when(employeeMapper.toDto(salvedEntity)).thenReturn(output);
	    
	    EmployeeDto result = employeeServiceImp.updateEmployeeByDocument(document, inputDto);
	    
	    assertNotNull(result);
	    assertEquals(output, result);
	    assertEquals("João Silva", result.name());
	    assertEquals("11111111111", result.document());
	    
	    verify(employeeRepository).findByDocument(document);
	    verify(employeeValidator).validateDocumentImmutable(document, inputDto.document());
	    verify(employeeMapper).updateFromDto(inputDto, entity);
	    verify(employeeRepository).save(entity);
	    verify(employeeMapper).toDto(salvedEntity);
	}
	
	@Test
	public void shouldThrowException_whenDocumentDoesNotExist_onUpdate() {
		
		String document = "00000000000";
	    
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
	    EmployeeDto inputDto = new EmployeeDto("João Silva", document, LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);

	    doThrow(new DocumentNotFoundException()).when(employeeValidator).validateDocumentExists(document);
	    
	    assertThrows(DocumentNotFoundException.class, () -> {
	    	employeeServiceImp.updateEmployeeByDocument(document, inputDto);
	    });
	    
	    verify(employeeRepository, never()).findByDocument(any());
	    verify(employeeMapper, never()).updateFromDto(any(), any());
	    verify(employeeRepository, never()).save(any());
		
	}
	
	@Test
	public void shouldThrowException_whenTryingToChangeDocument_onUpdate() {
		
		String originalDocument  = "11111111111";
		String newDocument = "99999999999";
		
	    Address address = new Address();
	    address.setStreet("Rua A");
	    address.setNumber(123L);
	    address.setCity("Cidade X");
	    address.setState("Estado Y");
	    address.setZipCode("12345-678");
	    address.setCountry("Brasil");
	    
    	AddressDto addressDto = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
	    EmployeeDto inputDto = new EmployeeDto("João Silva", newDocument, LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, addressDto);
	    Employee entity = new Employee(1L, "João Silva", originalDocument, LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);

	    doNothing().when(employeeValidator).validateDocumentExists(originalDocument);
	    when(employeeRepository.findByDocument(originalDocument)).thenReturn(entity);
	    
	    
	    doThrow(new DocumentImmutableException()).when(employeeValidator).validateDocumentImmutable(originalDocument, newDocument);
	    
	    assertThrows(DocumentImmutableException.class, () -> {
	    	employeeServiceImp.updateEmployeeByDocument(originalDocument, inputDto);
	    });
	    
	    verify(employeeRepository).findByDocument(originalDocument);
	    verify(employeeMapper, never()).updateFromDto(any(), any());
	    verify(employeeRepository, never()).save(any());
	}
}
