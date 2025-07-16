package br.com.postgreSQL.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Schema(description = "Objeto que representa um Endereço")
public record AddressDto(
		@Schema(description = "Nome da Rua", example = "Rua das Flores")
	    @NotBlank(message = "O campo street é obrigatório") String street,
	    @Schema(description = "Numero da casa", example = "123")
	    @NotNull(message = "O campo number é obrigatório") Long number,
	    @Schema(description = "Complemeto", example = "Apto 202, Bloco B")
	    @NotBlank(message = "O campo complement é obrigatório")String complement,
	    @Schema(description = "Bairro", example = "Itaparica")
	    @NotBlank(message = "O campo neighborhood é obrigatório") String neighborhood,
	    @Schema(description = "Cidade", example = "Maringa")
	    @NotBlank(message = "O campo city é obrigatório") String city,
	    @Schema(description = "Estado", example = "Parana")
	    @NotBlank(message = "O campo state é obrigatório") String state,
	    @Schema(description = "CEP", example = "01001-000")
	    @NotBlank(message = "O campo zipCode é obrigatório") String zipCode,
	    @Schema(description = "Pais", example = "Brazil")
	    @NotBlank(message = "O campo country é obrigatório") String country) {

}
