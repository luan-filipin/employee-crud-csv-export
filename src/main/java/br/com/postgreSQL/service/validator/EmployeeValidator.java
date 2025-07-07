package br.com.postgreSQL.service.validator;

import org.springframework.stereotype.Component;

import br.com.postgreSQL.exception.DocumentAlreadyExistsException;
import br.com.postgreSQL.exception.DocumentImmutableException;
import br.com.postgreSQL.exception.DocumentNotFoundException;
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
