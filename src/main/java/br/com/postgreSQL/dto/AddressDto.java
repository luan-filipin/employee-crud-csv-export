package br.com.postgreSQL.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDto(
	    @NotBlank(message = "O campo street é obrigatório") String street,
	    @NotNull(message = "O campo number é obrigatório") Long number,
	    @NotBlank(message = "O campo complement é obrigatório")String complement,
	    @NotBlank(message = "O campo neighborhood é obrigatório") String neighborhood,
	    @NotBlank(message = "O campo city é obrigatório") String city,
	    @NotBlank(message = "O campo state é obrigatório") String state,
	    @NotBlank(message = "O campo zipCode é obrigatório") String zipCode,
	    @NotBlank(message = "O campo country é obrigatório") String country) {

}
