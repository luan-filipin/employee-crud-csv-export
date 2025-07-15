package br.com.postgreSQL.service.validator.employee;

import org.springframework.stereotype.Component;

import br.com.postgreSQL.exception.employee.DocumentAlreadyExistsException;
import br.com.postgreSQL.exception.employee.DocumentImmutableException;
import br.com.postgreSQL.exception.employee.DocumentNotFoundException;
import br.com.postgreSQL.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {
	
	private final EmployeeRepository employeeRepository;

	public void validateDocumentExists(String document) {
		if(!employeeRepository.existsByDocument(document)) {
			throw new DocumentNotFoundException();
		}
	}
	
	public void validateDocumentNotExists(String document) {
		if(employeeRepository.existsByDocument(document)) {
			throw new DocumentAlreadyExistsException();
		}
	}
	
	public void validateDocumentImmutable(String originalDocument, String updateDocument) {
		if(!originalDocument.equals(updateDocument)) {
			throw new DocumentImmutableException();
		}
	}
}
