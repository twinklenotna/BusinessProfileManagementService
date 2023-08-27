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

  /**
   * To update the BusinessProfile
   * @param profile Profile object containing the fields to be changed
   */
  @Transactional
  public String updateProfile(BusinessProfile profile) {
    BusinessProfileRequest businessProfileRequest =
        profileRequestService.createBusinessProfileRequest(profile, RequestType.UPDATE, new HashSet<>());
    logger.info("created profile request with id: " + businessProfileRequest.getRequestId());
    sendProfileUpdateRequest(businessProfileRequest);
    return businessProfileRequest.getRequestId();
  }

  /**
   * Update the BusinessProfile and validate it against the subscriptions
   * @param profile profileObject
   * @param subscriptions subscriptions against which we need to validate profile
   */
  @Transactional
  public String updateProfile(BusinessProfile profile, Set<String> subscriptions) {
    BusinessProfileRequest businessProfileRequest =
        profileRequestService.createBusinessProfileRequest(profile, RequestType.SUBSCRIBE, subscriptions);
    sendProfileUpdateRequest(businessProfileRequest);
    return businessProfileRequest.getRequestId();
  }

  /**
   * Create the draft businessProfile and send kafka message to validate the profile and make it active
   * @param profile profileObject
   * @return profileId
   */
  @Transactional
  public String createProfileRequest(BusinessProfile profile) {
    BusinessProfileEntity businessProfileEntity = createBusinessProfileEntity(profile);
    BusinessProfileRequest businessProfileRequest =
        profileRequestService.createBusinessProfileRequest(profile, RequestType.CREATE, new HashSet<>());
    sendProfileUpdateRequest(businessProfileRequest);
    return businessProfileEntity.getProfileId();
  }

  /**
   * Delete businessProfile associated with given profileId
   * @param profileId profileID of the businessProfile
   */
  public void deleteProfile(String profileId) {
    try {
      profileRepository.delete(profileId);
    } catch (Exception ex) {
      logger.warn("Business profile with id: " + profileId + " not found");
      throw new BusinessProfileNotFoundException("Business profile with id: " + profileId + " not found");
    }
  }

  /**
   * Fetch business profile by profileId
   * @param profileId id of the business Profile
   * @return businessProfile Object
   */
  public BusinessProfile getProfileById(String profileId) {
    BusinessProfileEntity profileEntity = profileRepository.getProfileById(profileId);
    if(profileEntity == null) {
      logger.warn("Business profile with id: " + profileId + " not found");
      throw new BusinessProfileNotFoundException("Business profile with id: " + profileId + " not found");
    }
    return businessProfileMapper.entityToDto(profileEntity);
  }

  /**
   * Create and save the draft profile
   * @param profile profileObject
   * @return profile Entity
   */
  private BusinessProfileEntity createBusinessProfileEntity(BusinessProfile profile) {
    BusinessProfileEntity profileEntity = businessProfileMapper.dtoToEntity(profile);
    profileEntity.setStatus(ProfileStatus.DRAFT);
    return profileRepository.save(profileEntity);
  }

  /**
   * Update the business profile and activate it
   * @param profile profileObject
   * @return profile Entity
   */
  public BusinessProfileEntity updateBusinessProfileEntity(BusinessProfile profile) {
    BusinessProfileEntity profileEntity = businessProfileMapper.dtoToEntity(profile);
    profileEntity.setStatus(ProfileStatus.ACTIVE);
    return profileRepository.save(profileEntity);
  }

  /**
   * Send profile for async validations
   * @param request profile request Object
   */
  public void sendProfileUpdateRequest(BusinessProfileRequest request) {
    profileUpdateRequestProducer.sendProfileUpdateRequestWithKey(
        request.getProfileId(),
        request);
  }
}

