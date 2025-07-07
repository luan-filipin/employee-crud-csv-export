package br.com.postgreSQL.dto;

import java.time.LocalDate;

import br.com.postgreSQL.model.Address;
import br.com.postgreSQL.model.enums.EmployeeGender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeDto(
		@NotBlank(message = "O campo name é obrigatorio") String name,
		@NotBlank(message = "O campo document é obrigatorio") String document,
		@NotNull(message = "O campo birthDate é obrigatório") LocalDate birthDate,
		@NotNull(message = "O campo gender é obrigatorio") EmployeeGender employeeGender,
		@Valid// Valida os campos internos do Address.
		@NotNull(message = "O campo address é obrigatorio")Address address) {

}
