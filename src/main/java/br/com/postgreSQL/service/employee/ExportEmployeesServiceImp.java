package br.com.postgreSQL.service.employee;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import br.com.postgreSQL.model.Employee;
import br.com.postgreSQL.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExportEmployeesServiceImp implements ExportEmployeesService{

	private final EmployeeRepository employeeRepository;

	@Override
	public String exportEmployeesToCsv() {
		List<Employee> employees = employeeRepository.findAll();
		StringBuilder csvBuilder = new StringBuilder();
		
		csvBuilder.append("ID, Nome, Document\n");
		
		for (Employee emp : employees) {
			csvBuilder
			.append(emp.getId()).append(",")
			.append(escape(emp.getName())).append(",")
			.append(escape(emp.getDocument()))
			.append("\n");
			
		}
		
		return csvBuilder.toString();
	}
	
	
	private String escape(String value) {
	    if (value == null) return "";
	    if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
	        value = value.replace("\"", "\"\""); // duplica aspas internas
	        return "\"" + value + "\""; // envolve com aspas
	    }
	    return value;
	}
}
