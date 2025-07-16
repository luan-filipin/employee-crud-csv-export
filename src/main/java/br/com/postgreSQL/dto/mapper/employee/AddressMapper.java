package br.com.postgreSQL.dto.mapper.employee;

import org.mapstruct.Mapper;

import br.com.postgreSQL.dto.employee.AddressDto;
import br.com.postgreSQL.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
	
    AddressDto toDto(Address address);
    Address toEntity(AddressDto dto);

}
