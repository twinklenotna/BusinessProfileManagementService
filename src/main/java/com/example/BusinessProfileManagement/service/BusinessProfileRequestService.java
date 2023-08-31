package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileRequestException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchRequestMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestResponseMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessProfileRequestService {
  private  final BusinessProfileRequestRepository businessProfileRequestRepository;
  private  final BusinessProfileRequestMapper businessProfileRequestMapper;
  private  final BusinessProfileProductValidationService businessProfileProductValidationService;
  private  final BusinessProfileRequestResponseMapper businessProfileRequestResponseMapper;
  private  final BusinessProfilePatchRequestMapper businessProfilePatchRequestMapper;


  /**
   * To get all the profile update requests for a profile
   * @param profileId profileId
   * @return List of profile update requests
   */
  public List<BusinessProfileUpdateRequest> getProfileUpdateRequestsByprofileId(String profileId) {
    List<BusinessProfileRequestEntity> businessProfileRequestEntities = businessProfileRequestRepository.findByprofileId(profileId);
    List<BusinessProfileUpdateRequest> businessProfileUpdateRequests = new ArrayList<>();
    for(BusinessProfileRequestEntity businessProfileRequestEntity : businessProfileRequestEntities) {
      businessProfileUpdateRequests.add(businessProfileRequestMapper.entityToDto(businessProfileRequestEntity));
    }
    return businessProfileUpdateRequests;
  }

  /**
   * To get the profile update request by requestId
   * @param requestId requestId
   * @return profile update request
   */
  public BusinessProfileRequestResponse getProfileUpdateRequest(String requestId) {
    BusinessProfileRequestEntity requestEntity = businessProfileRequestRepository.findByRequestId(requestId);
    List<BusinessProfileRequestProductValidation> businessProfileRequestProductValidations =
        businessProfileProductValidationService.getRequestProductValidations(requestId);
    if(requestEntity == null) {
      log.error("Business profile request with id: " + requestId + " not found");
      throw new BusinessProfileRequestNotFoundException("Business profile request with id: " + requestId + " not found");
    }
    BusinessProfileRequestResponse response = businessProfileRequestResponseMapper.entityToDto(requestEntity);
    response.setSubscriptionValidations(businessProfileRequestProductValidations);
    return response;
  }

  /**
   * To update the status of the update profile request
   * @param request request
   * @param status status
   */
  public void updateRequestStatus(BusinessProfileUpdateRequest request, ApprovalStatus status) {
    try {
      request.setStatus(status);
      updateBusinessProfileRequestEntity(request);
      log.info("Profile request: {} is updated with status: {}",request.getRequestId(),request.getStatus());
    } catch(Exception ex) {
      log.error("Error happened while updating request with requestId: "+request.getRequestId()+" " +ex.getMessage());
      throw new BusinessProfileRequestException(
          "Error happened while updating request with requestId: "
              + request.getRequestId()
              + " "
              + ex.getMessage());
    }
  }

  /**
   * To create a profile update request
   * @param profile profile
   * @param requestType requestType
   * @param subscriptions subscriptions
   * @return profile update request
   */
  BusinessProfileUpdateRequest createBusinessProfileRequest(BusinessProfilePatchRequest profile, RequestType requestType, Set<String> subscriptions) {
    BusinessProfileRequestEntity requestEntity = new BusinessProfileRequestEntity();
    requestEntity.setBusinessProfile(businessProfilePatchRequestMapper.toEntity(profile));
    requestEntity.setProfileId(profile.getProfileId());
    requestEntity.setStatus(ApprovalStatus.IN_PROGRESS);
    requestEntity.setRequestType(requestType);
    requestEntity.setSubscriptions(subscriptions);
    try{
      log.info("Creating Profile request: {} with status: {}",requestEntity.getRequestId(),requestEntity.getStatus());
       return businessProfileRequestMapper.entityToDto(businessProfileRequestRepository.saveAndUpdate(requestEntity));
    } catch(Exception ex) {
      log.error("Error happened while creating request with requestId for profileId: "+ profile.getProfileId() + " " +ex.getMessage());
      throw new RuntimeException("Error happened while creating request with requestId for profileId: "+ profile.getProfileId() + " " +ex.getMessage(), ex);
    }
  }

  public BusinessProfileRequestEntity updateBusinessProfileRequestEntity(BusinessProfileUpdateRequest businessProfileUpdateRequest) {
    return businessProfileRequestRepository.saveAndUpdate(businessProfileRequestMapper.dtoToEntity(businessProfileUpdateRequest));
  }

}
