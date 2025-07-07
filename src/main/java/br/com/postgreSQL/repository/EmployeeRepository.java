package br.com.postgreSQL.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.postgreSQL.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	boolean existsByDocument(String document);
	
	Employee findByDocument(String document);
		
	void deleteAllByDocument(String document);
}
