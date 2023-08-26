package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface BusinessProfileRequestProductValidationMapper {
  BusinessProfileRequestProductValidationMapper INSTANCE = Mappers.getMapper(BusinessProfileRequestProductValidationMapper.class);
  BusinessProfileRequestProductValidation entityToDto(BusinessProfileRequestProductValidationEntity entity);

  BusinessProfileRequestProductValidationEntity dtoToEntity(BusinessProfileRequestProductValidation dto);
}
