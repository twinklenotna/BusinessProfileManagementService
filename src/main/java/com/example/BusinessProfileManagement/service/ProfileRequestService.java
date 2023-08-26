package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ProfileRequestService {
  Logger logger = LoggerFactory.getLogger(ProfileRequestService.class);
  final BusinessProfileRequestRepository _businessProfileRequestRepository;
  final BusinessProfilePatchMapper businessProfilePatchMapper;
  final BusinessProfileRequestMapper _businessProfileRequestMapper;

  public ProfileRequestService(
      BusinessProfileRequestRepository businessProfileRequestRepository,
      BusinessProfilePatchMapper businessProfilePatchMapper, BusinessProfileRequestMapper businessProfileRequestMapper) {
    _businessProfileRequestRepository = businessProfileRequestRepository;
    this.businessProfilePatchMapper = businessProfilePatchMapper;
    _businessProfileRequestMapper = businessProfileRequestMapper;
  }

  public List<BusinessProfileRequest> getProfileRequestByProfileId(String profileId) {
    List<BusinessProfileRequestEntity> businessProfileRequestEntities = _businessProfileRequestRepository.findByProfileId(profileId);
    List<BusinessProfileRequest> businessProfileRequests = new ArrayList<>();
    for(BusinessProfileRequestEntity businessProfileRequestEntity : businessProfileRequestEntities) {
      businessProfileRequests.add(_businessProfileRequestMapper.entityToDto(businessProfileRequestEntity));
    }
    return businessProfileRequests;
  }

  public BusinessProfileRequest getProfileRequest(String requestId) {
    BusinessProfileRequestEntity requestEntity = _businessProfileRequestRepository.findByRequestId(requestId);
    if(requestEntity == null) {
      logger.error("Business profile request with id: " + requestId + " not found");
      throw new BusinessProfileRequestNotFoundException("Business profile request with id: " + requestId + " not found");
    }
    return _businessProfileRequestMapper.entityToDto(requestEntity);
  }
  public void updateRequestStatus(BusinessProfileRequest request, ApprovalStatus status) {
    try {
      request.setStatus(status);
      updateBusinessProfileRequestEntity(request);
    } catch(Exception ex) {
      logger.error("Error happened while updating request with requestId: "+request.getRequestId()+" " +ex.getMessage());
    }
  }

  BusinessProfileRequestEntity createBusinessProfileRequestEntity(BusinessProfile profile, RequestType requestType, Set<String> subscriptions) {
    BusinessProfileRequestEntity requestEntity = new BusinessProfileRequestEntity();
    requestEntity.setBusinessProfile(businessProfilePatchMapper.dtoToEntity(profile));
    requestEntity.setProfileId(profile.getProfileId());
    requestEntity.setStatus(ApprovalStatus.IN_PROGRESS);
    requestEntity.setRequestType(requestType);
    requestEntity.setSubscriptions(subscriptions);
    try{
      return _businessProfileRequestRepository.saveAndUpdate(requestEntity);
    } catch(Exception ex) {
      logger.error("Error happened while creating request with requestId for profileId: "+ profile.getProfileId() + " " +ex.getMessage());
      throw new RuntimeException("Error happened while creating request with requestId for profileId: "+ profile.getProfileId() + " " +ex.getMessage(), ex);
    }
  }

  public BusinessProfileRequestEntity updateBusinessProfileRequestEntity(BusinessProfileRequest businessProfileRequest) {
    return _businessProfileRequestRepository.saveAndUpdate(_businessProfileRequestMapper.dtoToEntity(businessProfileRequest));
  }

}
