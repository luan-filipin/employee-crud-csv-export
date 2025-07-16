package br.com.postgreSQL.dto.employee;

import java.time.LocalDate;

import br.com.postgreSQL.model.enums.EmployeeGender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Objeto que representa um Funcionário")
public record EmployeeDto(
		@Schema(description = "Nome completo", example = "João da Silva")
		@NotBlank(message = "O campo name é obrigatorio") String name,
		@Schema(description = "Documento CPF", example = "18192021222")
		@NotBlank(message = "O campo document é obrigatorio") String document,
		@Schema(description = "Data de aniversario", example = "1990-05-20")
		@NotNull(message = "O campo birthDate é obrigatório") LocalDate birthDate,
		@Schema(description = "Genero", example = "HOMEM")
		@NotNull(message = "O campo gender é obrigatorio") EmployeeGender employeeGender,
		@NotNull(message = "O campo address é obrigatorio") AddressDto address) {
}
