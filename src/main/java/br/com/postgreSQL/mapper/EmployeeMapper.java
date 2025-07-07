package br.com.postgreSQL.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import br.com.postgreSQL.dto.EmployeeDto;
import br.com.postgreSQL.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	EmployeeDto toDto(Employee employee);
	
	Employee toEntity(EmployeeDto employeeDto);
	
	List<EmployeeDto> toDtoList(List<Employee> list);
	
	@Mapping(target = "document", ignore = true)//Faz ignorar o campo document para update.
	void updateFromDto(EmployeeDto dto, @MappingTarget Employee entity);

}
