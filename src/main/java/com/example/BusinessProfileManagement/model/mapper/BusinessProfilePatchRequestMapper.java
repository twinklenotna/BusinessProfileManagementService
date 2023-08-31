package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface BusinessProfilePatchRequestMapper {
  @Mappings({
      @Mapping(source = "taxInfoEntity", target = "taxInfo"),
  })
  BusinessProfilePatchRequest toDto(BusinessProfileEntity entity);

  @Mappings({
      @Mapping(source = "taxInfo", target = "taxInfoEntity"),
  })
  BusinessProfileEntity toEntity(BusinessProfilePatchRequest dto);
}
