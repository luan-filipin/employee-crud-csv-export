package br.com.postgreSQL.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.postgreSQL.config.GlobalExceptionHandler;
import br.com.postgreSQL.dto.employee.AddressDto;
import br.com.postgreSQL.dto.employee.EmployeeDto;
import br.com.postgreSQL.dto.employee.EmployeeDtoListWrapper;
import br.com.postgreSQL.exception.employee.DocumentAlreadyExistsException;
import br.com.postgreSQL.exception.employee.DocumentNotFoundException;
import br.com.postgreSQL.model.enums.EmployeeGender;
import br.com.postgreSQL.service.employee.EmployeeServiceImp;

@WebMvcTest(EmployeeController.class)
@ExtendWith(SpringExtension.class)
@Import(GlobalExceptionHandler.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeServiceImp employeeServiceImp;

    @Test
    public void shouldCreateEmployeeSuccessfully() throws Exception {

        // Configurar ObjectMapper para datas (caso necessário)
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    	AddressDto address = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
        EmployeeDto inputDto = new EmployeeDto("João Silva", "11111111111",
                LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);

        EmployeeDto savedDto = new EmployeeDto("João Silva", "11111111111",
                LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);

        when(employeeServiceImp.createEmployee(any(EmployeeDto.class))).thenReturn(savedDto);

        mockMvc.perform(post("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.document").value("11111111111"))
                .andExpect(jsonPath("$.address.street").value("Rua A"));

        verify(employeeServiceImp).createEmployee(any(EmployeeDto.class));
    }
    
    @Test
    public void shouldReturnBadRequestWhenCreateEmployeeWithInvalidDto() throws Exception{
        
    	AddressDto address = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");        
        EmployeeDto invalidDto = new EmployeeDto("", "11111111111",
                LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
        
        mockMvc.perform(post("/api/employee")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void shouldReturnConflictWhenCreateEmployeeWithDuplicateDocument()throws Exception{
    	
    	AddressDto address = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");        
        EmployeeDto invalidDto = new EmployeeDto("João Silva", "11111111111",
                LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
        
        when(employeeServiceImp.createEmployee(any(EmployeeDto.class))).thenThrow(new DocumentAlreadyExistsException());
        
        mockMvc.perform(post("/api/employee")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(objectMapper.writeValueAsString(invalidDto)))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Este documento já existe!"))
        .andExpect(jsonPath("$.status").value(409))
        .andExpect(jsonPath("$.path").value("/api/employee"))
        .andExpect(jsonPath("$.timestamp").exists());
        
        verify(employeeServiceImp).createEmployee(any(EmployeeDto.class));
    }
    
    @Test
    public void shouldCreateMultipleEmployeesSuccessfully() throws Exception {
        
        AddressDto address = new AddressDto(
            "Rua A",
            123L,
            "Complemento X",
            "Bairro Y",
            "Cidade X",
            "Estado Y",
            "12345-678",
            "Brasil"
        );
        
        EmployeeDto employeeDto1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
        EmployeeDto employeeDto2 = new EmployeeDto("Maria Lima", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
        List<EmployeeDto> employees = List.of(employeeDto1, employeeDto2);

        EmployeeDtoListWrapper wrapper = new EmployeeDtoListWrapper();
        wrapper.setEmployees(employees);

        when(employeeServiceImp.createListEmployee(anyList())).thenReturn(employees);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrapper)))  // aqui passa o wrapper, não a lista direta
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("João Silva"))
            .andExpect(jsonPath("$[1].name").value("Maria Lima"));

        verify(employeeServiceImp).createListEmployee(anyList());
    }

    
    @Test
    public void shouldReturnBadRequestWhenCreatingEmployeesWithInvalidData() throws Exception {
        
        AddressDto address = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
        
        EmployeeDto invalidEmployee1 = new EmployeeDto("", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
        EmployeeDto invalidEmployee2 = new EmployeeDto("", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
        
        EmployeeDtoListWrapper wrapper = new EmployeeDtoListWrapper();
        wrapper.setEmployees(List.of(invalidEmployee1, invalidEmployee2));
        
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrapper)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors").isArray())
            .andExpect(jsonPath("$.errors.length()").value(2))
            .andExpect(jsonPath("$.errors[*].field").value(org.hamcrest.Matchers.containsInAnyOrder("employees[0].name", "employees[1].name")))
            .andExpect(jsonPath("$.errors[0].message").exists())
            .andExpect(jsonPath("$.errors[1].message").exists());

    }
    
    @Test
    public void shouldReturnConflictWhenCreateEmployeesWithDuplicateDocument()throws Exception{
    	  
    	AddressDto address = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
          
          EmployeeDto employeeDto1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
          EmployeeDto employeeDto2 = new EmployeeDto("Maria Lima", "11111111111", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
          
          List<EmployeeDto> listEmployee = List.of(employeeDto1, employeeDto2);
          
          EmployeeDtoListWrapper wrapper = new EmployeeDtoListWrapper();
          wrapper.setEmployees(listEmployee);
          
          when(employeeServiceImp.createListEmployee(anyList())).thenThrow(new DocumentAlreadyExistsException());
          
          mockMvc.perform(post("/api/employees")
        		  .contentType(MediaType.APPLICATION_JSON)
        		  .content(objectMapper.writeValueAsString(wrapper)))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.message").value("Este documento já existe!"))
          .andExpect(jsonPath("$.status").value(409))
          .andExpect(jsonPath("$.path").value("/api/employees"))
          .andExpect(jsonPath("$.timestamp").exists());
          
          verify(employeeServiceImp).createListEmployee(anyList());
        		  
    }
    
    @Test
    public void shouldReturnEmployeeWhenDocumentIsValid()throws Exception{
    	
    	String document = "11111111111";
    	
    	AddressDto address = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
        EmployeeDto employeeDto = new EmployeeDto("João Silva", document, LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);

        when(employeeServiceImp.findEmployeeByDocument(document)).thenReturn(employeeDto);
        
        mockMvc.perform(get("/api/employee/{document}", document)
        		.contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("João Silva"))
        .andExpect(jsonPath("$.document").value(document))
        .andExpect(jsonPath("$.address.street").value("Rua A"));
        
        verify(employeeServiceImp).findEmployeeByDocument(document);
    }

    @Test
    public void shouldReturnNotFoundWhenDocumentDoesNotExist() throws Exception {
    	String document = "00000000000";
    	
    	when(employeeServiceImp.findEmployeeByDocument(document)).thenThrow(new DocumentNotFoundException());
    	
    	mockMvc.perform(get("/api/employee/{document}", document)
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isNotFound())
    	.andExpect(jsonPath("$.message").value("Este documento não existe!"))
    	.andExpect(jsonPath("$.status").value(404))
    	.andExpect(jsonPath("$.path").value("/api/employee/"+ document))
    	.andExpect(jsonPath("$.timestamp").exists());
    	
    	verify(employeeServiceImp).findEmployeeByDocument(document);
    	
    }
    
    @Test
    public void shouldReturnPagedListOfEmployeesSuccessfully() throws Exception {
    	
    	AddressDto address = new AddressDto("Rua A", 123L, "Teste", "itaparica", "Cidade X", "Estado Y", "12345-678", "Brasil");
        
        EmployeeDto pageEmployee1 = new EmployeeDto("João Silva", "11111111111", LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);
        EmployeeDto pageEmployee2 = new EmployeeDto("Maria Souza", "22222222222", LocalDate.of(1992, 2, 2), EmployeeGender.MULHER, address);
        
        List<EmployeeDto> employees = List.of(pageEmployee1, pageEmployee2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<EmployeeDto> page = new PageImpl<>(employees, pageable, employees.size());

        
        when(employeeServiceImp.findAllEmployees(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);
        
        mockMvc.perform(get("/api/employees")
        		.param("page", "0")
        		.param("size", "10")
        		.param("sort", "name, asc")
        		.contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.length()").value(2))
        .andExpect(jsonPath("$.content[0].name").value("João Silva"))
        .andExpect(jsonPath("$.content[1].name").value("Maria Souza"))
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.totalPages").value(1))
        .andExpect(jsonPath("$.size").value(10))
        .andExpect(jsonPath("$.number").value(0));
        
        verify(employeeServiceImp).findAllEmployees(any(org.springframework.data.domain.Pageable.class));

    }
    
    @Test
    public void shouldReturnInternalServerErrorWhenServiceFailsOnFindAll()throws Exception{
    	
    	when(employeeServiceImp.findAllEmployees(any(Pageable.class))).thenThrow(new RuntimeException("Erro inesperado"));
    	
    	mockMvc.perform(get("/api/employees")
    			.param("page", "0")
    			.param("size", "10")
    			.param("sort", "name,asc")
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isInternalServerError())
    	.andExpect(jsonPath("$.message").value("Erro inesperado"))
    	.andExpect(jsonPath("$.status").value(500))
    	.andExpect(jsonPath("$.path").value("/api/employees"))
    	.andExpect(jsonPath("$.timestamp").exists());
    	
    	verify(employeeServiceImp).findAllEmployees(any(Pageable.class));
    }
    
    @Test
    public void shouldDeleteEmployeeSuccessfullyWhenDocumentExists()throws Exception{
    	
    	String document = "11111111111";
    	
    	mockMvc.perform(delete("/api/employee/{document}", document))
    	.andExpect(status().isNoContent());
    	
    	verify(employeeServiceImp).deleteEmployeeByDocument(document);
    	
    	
    }
    
    @Test
    public void shouldReturnNotFoundWhenDeletingNonExistentEmployee()throws Exception{
    	
    	String document = "11111111111";
    	
    	doThrow(new DocumentNotFoundException()).when(employeeServiceImp).deleteEmployeeByDocument(document);
    	
    	mockMvc.perform(delete("/api/employee/{document}", document))
    	.andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Este documento não existe!"))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.path").value("/api/employee/" + document))
        .andExpect(jsonPath("$.timestamp").exists());
    	
    	verify(employeeServiceImp).deleteEmployeeByDocument(document);
    }
    
    @Test
    public void shouldUpdateEmployeeSuccessfullyWhenDocumentExists() throws Exception {
        String document = "11111111111";

        AddressDto address = new AddressDto("Rua B", 456L, "Apto 101", "Centro", "Cidade Y", "Estado Z", "98765-432", "Brasil");
        EmployeeDto updatedDto = new EmployeeDto("João Atualizado", document, LocalDate.of(1990, 1, 1), EmployeeGender.HOMEM, address);

        when(employeeServiceImp.updateEmployeeByDocument(eq(document), any(EmployeeDto.class)))
            .thenReturn(updatedDto);

        mockMvc.perform(put("/api/employee/{document}", document)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("João Atualizado"))
            .andExpect(jsonPath("$.document").value(document))
            .andExpect(jsonPath("$.address.street").value("Rua B"));

        verify(employeeServiceImp).updateEmployeeByDocument(eq(document), any(EmployeeDto.class));
    }
    
    @Test
    public void shouldReturnNotFoundWhenUpdatingNonExistentEmployee() throws Exception {
        String document = "00000000000";

        AddressDto address = new AddressDto("Rua C", 789L, "Bloco C", "Bairro Z", "Cidade W", "Estado K", "65432-100", "Brasil");
        EmployeeDto updatedDto = new EmployeeDto("Nome Qualquer", document, LocalDate.of(1995, 5, 5), EmployeeGender.MULHER, address);

        when(employeeServiceImp.updateEmployeeByDocument(eq(document), any(EmployeeDto.class)))
            .thenThrow(new DocumentNotFoundException());

        mockMvc.perform(put("/api/employee/{document}", document)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Este documento não existe!"))
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.path").value("/api/employee/" + document))
            .andExpect(jsonPath("$.timestamp").exists());

        verify(employeeServiceImp).updateEmployeeByDocument(eq(document), any(EmployeeDto.class));
    }


}





























