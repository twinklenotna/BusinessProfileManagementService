package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.kafka.ProfileUpdateRequestProducer;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRepository;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusinessProfileService {
  Logger logger = LoggerFactory.getLogger(ProfileSubscriptionService.class);
  final BusinessProfileRepository profileRepository;
  final ProfileRequestService profileRequestService;
  final ProfileUpdateRequestProducer profileUpdateRequestProducer;
  final BusinessProfileMapper businessProfileMapper;
  final BusinessProfileRequestMapper businessProfileRequestMapper;

  @Autowired
  public BusinessProfileService(BusinessProfileRepository profileRepository,
      ProfileRequestService profileRequestService, ProfileUpdateRequestProducer profileUpdateRequestProducer,
      BusinessProfileMapper businessProfileMapper, BusinessProfileRequestMapper businessProfileRequestMapper) {
    this.profileRepository = profileRepository;
    this.profileRequestService = profileRequestService;
    this.profileUpdateRequestProducer = profileUpdateRequestProducer;
    this.businessProfileMapper = businessProfileMapper;
    this.businessProfileRequestMapper = businessProfileRequestMapper;
  }

  public void updateProfile(BusinessProfile profile) {
    BusinessProfileRequest businessProfileRequest =
        profileRequestService.createBusinessProfileRequest(profile, RequestType.UPDATE, new HashSet<>());
    logger.info("created profile request with id: " + businessProfileRequest.getRequestId());
    sendProfileUpdateRequest(businessProfileRequest);
  }

  public void updateProfile(BusinessProfile profile, Set<String> subscriptions) {
    BusinessProfileRequest businessProfileRequest =
        profileRequestService.createBusinessProfileRequest(profile, RequestType.UPDATE, subscriptions);
    sendProfileUpdateRequest(businessProfileRequest);
  }

  @Transactional
  public String createProfileRequest(BusinessProfile profile) {
    BusinessProfileEntity businessProfileEntity = createBusinessProfileEntity(profile);
    BusinessProfileRequest businessProfileRequest =
        profileRequestService.createBusinessProfileRequest(profile, RequestType.UPDATE, new HashSet<>());
    sendProfileUpdateRequest(businessProfileRequest);
    return businessProfileEntity.getProfileId();
  }

  public void deleteProfile(String profileId) {
    try {
      profileRepository.delete(profileId);
    } catch (Exception ex) {
      logger.warn("Business profile with id: " + profileId + " not found");
      throw new BusinessProfileNotFoundException("Business profile with id: " + profileId + " not found");
    }
  }

  public BusinessProfile getProfileById(String profileId) {
    BusinessProfileEntity profileEntity = profileRepository.getProfileById(profileId);
    if(profileEntity == null) {
      logger.warn("Business profile with id: " + profileId + " not found");
      throw new BusinessProfileNotFoundException("Business profile with id: " + profileId + " not found");
    }
    return businessProfileMapper.entityToDto(profileEntity);
  }

  private BusinessProfileEntity createBusinessProfileEntity(BusinessProfile profile) {
    BusinessProfileEntity profileEntity = businessProfileMapper.dtoToEntity(profile);
    profileEntity.setStatus(ProfileStatus.DRAFT);
    return profileRepository.save(profileEntity);
  }

  public BusinessProfileEntity updateBusinessProfileEntity(BusinessProfile profile) {
    BusinessProfileEntity profileEntity = businessProfileMapper.dtoToEntity(profile);
    profileEntity.setStatus(ProfileStatus.ACTIVE);
    return profileRepository.save(profileEntity);
  }

  public void sendProfileUpdateRequest(BusinessProfileRequest request) {
    profileUpdateRequestProducer.sendProfileUpdateRequestWithKey(
        request.getProfileId(),
        request);
  }
}

