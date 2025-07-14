package br.com.postgreSQL.dto;

import java.util.List;

import jakarta.validation.Valid;
import lombok.Data;
@Data
public class EmployeeDtoListWrapper {

	@Valid
	private List<EmployeeDto> employees;
}
