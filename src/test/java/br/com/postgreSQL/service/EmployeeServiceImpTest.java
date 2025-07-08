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
        
        EmployeeDto inputDto = new EmployeeDto("João Silva", 
				"12345678900",
				LocalDate.of(1990, 1, 1),
				EmployeeGender.HOMEM,
	            address
	    );
        
        Employee entity = new Employee();
        entity.setName("João Silva");
        entity.setDocument("12345678900");
        entity.setBirthDate(LocalDate.of(1990, 1, 1));
        entity.setEmployeeGender(EmployeeGender.HOMEM);
        entity.setAddress(address);
        
        Employee savedEntity = new Employee();
        savedEntity.setId(1L);
        savedEntity.setName("João Silva");
        savedEntity.setDocument("12345678900");
        savedEntity.setBirthDate(LocalDate.of(1990, 1, 1));
        savedEntity.setEmployeeGender(EmployeeGender.HOMEM);
        savedEntity.setAddress(address);
	
		EmployeeDto outputDto = new EmployeeDto("João Silva", 
				"12345678900",
				LocalDate.of(1990, 1, 1),
				EmployeeGender.HOMEM,
				address
	    );
		
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
		
		Address address = new Address();
        address.setStreet("Rua A");
        address.setNumber(123L);
        address.setCity("Cidade X");
        address.setState("Estado Y");
        address.setZipCode("12345-678");
        address.setCountry("Brasil");
        
        EmployeeDto inputDto = new EmployeeDto("João Silva", 
				"12345678900",
				LocalDate.of(1990, 1, 1),
				EmployeeGender.HOMEM,
	            address
	    );
        
        doThrow(new DocumentAlreadyExistsException())
        .when(employeeValidator)
        .validateDocumentNotExists("12345678900");
        
        assertThrows(DocumentAlreadyExistsException.class, () ->{
        	employeeServiceImp.createEmployee(inputDto);
        });
        
        verify(employeeRepository, never()).save(any());
	}
	
}
