package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = BusinessProfileMapper.class)
public interface BusinessProfileRequestMapper {
  BusinessProfileUpdateRequest entityToDto(BusinessProfileRequestEntity entity);

  BusinessProfileRequestEntity dtoToEntity(BusinessProfileUpdateRequest dto);
}
