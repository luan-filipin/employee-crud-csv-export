package br.com.postgreSQL.dto.employee;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.Data;

@Schema(description = "Objeto que representa uma lista de funcionarios")
@Data
public class EmployeeDtoListWrapper {

	@Schema(description = "Lista de funcionarios")
	@Valid
	private List<EmployeeDto> employees;
}
