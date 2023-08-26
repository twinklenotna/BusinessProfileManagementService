package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.entity.BusinessProfilePatchRequestEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface BusinessProfilePatchMapper {

  BusinessProfilePatchRequestEntity dtoToEntity(BusinessProfile dto);
  BusinessProfile entityToDto(BusinessProfilePatchRequestEntity entity);
}
