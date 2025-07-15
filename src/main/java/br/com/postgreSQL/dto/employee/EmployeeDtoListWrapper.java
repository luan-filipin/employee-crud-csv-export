package br.com.postgreSQL.dto.employee;

import java.util.List;

import jakarta.validation.Valid;
import lombok.Data;
@Data
public class EmployeeDtoListWrapper {

	@Valid
	private List<EmployeeDto> employees;
}
