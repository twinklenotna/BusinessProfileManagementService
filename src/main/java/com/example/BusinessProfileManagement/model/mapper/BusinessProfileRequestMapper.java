package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = BusinessProfileMapper.class)
public interface BusinessProfileRequestMapper {
  BusinessProfileRequest entityToDto(BusinessProfileRequestEntity entity);

  BusinessProfileRequestEntity dtoToEntity(BusinessProfileRequest dto);
}
