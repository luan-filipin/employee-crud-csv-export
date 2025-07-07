package br.com.postgreSQL.model;

import java.time.LocalDate;

import br.com.postgreSQL.model.enums.EmployeeGender;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "postgres")
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String document;
	@Column(nullable = false)
	private LocalDate birthDate;
	@Column(nullable = false)
	private EmployeeGender employeeGender;
	
	@Embedded
	private Address address;
}
