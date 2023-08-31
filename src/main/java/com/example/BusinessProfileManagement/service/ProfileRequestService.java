package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileRequestException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchRequestMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestResponseMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileRequestService {
  Logger logger = LoggerFactory.getLogger(ProfileRequestService.class);
  private  final BusinessProfileRequestRepository businessProfileRequestRepository;
  private  final BusinessProfileMapper businessProfileMapper;
  private  final BusinessProfileRequestMapper businessProfileRequestMapper;
  private  final ProfileProductValidationService profileProductValidationService;
  private  final BusinessProfileRequestResponseMapper businessProfileRequestResponseMapper;
  private  final BusinessProfilePatchRequestMapper businessProfilePatchRequestMapper;


  public List<BusinessProfileUpdateRequest> getProfileUpdateRequestsByprofileId(String profileId) {
    List<BusinessProfileRequestEntity> businessProfileRequestEntities = businessProfileRequestRepository.findByprofileId(profileId);
    List<BusinessProfileUpdateRequest> businessProfileUpdateRequests = new ArrayList<>();
    for(BusinessProfileRequestEntity businessProfileRequestEntity : businessProfileRequestEntities) {
      businessProfileUpdateRequests.add(businessProfileRequestMapper.entityToDto(businessProfileRequestEntity));
    }
    return businessProfileUpdateRequests;
  }

  public BusinessProfileRequestResponse getProfileUpdateRequest(String requestId) {
    BusinessProfileRequestEntity requestEntity = businessProfileRequestRepository.findByRequestId(requestId);
    List<BusinessProfileRequestProductValidation> businessProfileRequestProductValidations =
        profileProductValidationService.getRequestProductValidations(requestId);
    if(requestEntity == null) {
      logger.error("Business profile request with id: " + requestId + " not found");
      throw new BusinessProfileRequestNotFoundException("Business profile request with id: " + requestId + " not found");
    }
    BusinessProfileRequestResponse response = businessProfileRequestResponseMapper.entityToDto(requestEntity);
    response.setSubscriptionValidations(businessProfileRequestProductValidations);
    return response;
  }
  public void updateRequestStatus(BusinessProfileUpdateRequest request, ApprovalStatus status) {
    try {
      request.setStatus(status);
      updateBusinessProfileRequestEntity(request);
    } catch(Exception ex) {
      logger.error("Error happened while updating request with requestId: "+request.getRequestId()+" " +ex.getMessage());
      throw new BusinessProfileRequestException(
          "Error happened while updating request with requestId: "
              + request.getRequestId()
              + " "
              + ex.getMessage());
    }
  }

  BusinessProfileUpdateRequest createBusinessProfileRequest(BusinessProfilePatchRequest profile, RequestType requestType, Set<String> subscriptions) {
    BusinessProfileRequestEntity requestEntity = new BusinessProfileRequestEntity();
    requestEntity.setBusinessProfile(businessProfilePatchRequestMapper.toEntity(profile));
    requestEntity.setProfileId(profile.getProfileId());
    requestEntity.setStatus(ApprovalStatus.IN_PROGRESS);
    requestEntity.setRequestType(requestType);
    requestEntity.setSubscriptions(subscriptions);
    try{
       return businessProfileRequestMapper.entityToDto(businessProfileRequestRepository.saveAndUpdate(requestEntity));
    } catch(Exception ex) {
      logger.error("Error happened while creating request with requestId for profileId: "+ profile.getProfileId() + " " +ex.getMessage());
      throw new RuntimeException("Error happened while creating request with requestId for profileId: "+ profile.getProfileId() + " " +ex.getMessage(), ex);
    }
  }

  public BusinessProfileRequestEntity updateBusinessProfileRequestEntity(BusinessProfileUpdateRequest businessProfileUpdateRequest) {
    return businessProfileRequestRepository.saveAndUpdate(businessProfileRequestMapper.dtoToEntity(businessProfileUpdateRequest));
  }

}
