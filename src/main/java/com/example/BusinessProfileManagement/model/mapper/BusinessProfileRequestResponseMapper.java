package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = BusinessProfileMapper.class)
public interface BusinessProfileRequestResponseMapper {
  BusinessProfileRequestResponse entityToDto(BusinessProfileRequestEntity entity);

  BusinessProfileRequestEntity dtoToEntity(BusinessProfileRequestResponse dto);
}
