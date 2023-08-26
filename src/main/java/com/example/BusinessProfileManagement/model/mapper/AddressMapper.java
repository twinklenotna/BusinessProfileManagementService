package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.Address;
import com.example.BusinessProfileManagement.model.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface AddressMapper {
  Address entityToDto(AddressEntity entity);

  AddressEntity dtoToEntity(Address dto);
}
