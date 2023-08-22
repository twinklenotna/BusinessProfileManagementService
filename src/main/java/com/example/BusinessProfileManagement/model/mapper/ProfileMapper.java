package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.ProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring") // "spring" generates Spring components
public interface ProfileMapper {

  @Mappings({
      @Mapping(target = "profileId", source = "profileId"),
      @Mapping(target = "companyName", source = "companyName"),
      @Mapping(target = "legalName", source = "legalName"),
      @Mapping(target = "businessAddress", source = "businessAddress"),
      @Mapping(target = "legalAddress", source = "legalAddress"),
      @Mapping(target = "pan", source = "pan"),
      @Mapping(target = "ein", source = "ein"),
      @Mapping(target = "email", source = "email"),
      @Mapping(target = "website", source = "website"),
      @Mapping(target = "status", source = "status") // Map the status from the source
  })
  BusinessProfile profileUpdateRequestToBusinessProfile(ProfileUpdateRequest request, ApprovalStatus status);
}


