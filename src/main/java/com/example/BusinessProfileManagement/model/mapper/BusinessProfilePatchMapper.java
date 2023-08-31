package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface BusinessProfilePatchMapper {
  BusinessProfilePatchRequest toPatchRequest(BusinessProfile entity);
  BusinessProfile toProfile(BusinessProfilePatchRequest dto);
}
