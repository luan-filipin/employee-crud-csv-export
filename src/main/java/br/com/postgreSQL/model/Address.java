package br.com.postgreSQL.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {

	private String street;
	private Long number;
	private String complement;
	private String neighborhood;
	private String city;
	private String state;
	private String zipCode;
	private String country;
}
