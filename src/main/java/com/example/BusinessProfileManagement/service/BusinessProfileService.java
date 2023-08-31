package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.kafka.ProfileUpdateRequestProducer;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRepository;
import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessProfileService {
  Logger logger = LoggerFactory.getLogger(ProfileSubscriptionService.class);
  private final BusinessProfileRepository profileRepository;
  private final BusinessProfileRequestService _businessProfileRequestService;
  private final ProfileUpdateRequestProducer profileUpdateRequestProducer;
  private final BusinessProfileMapper businessProfileMapper;
  private final BusinessProfilePatchMapper businessProfilePatchMapper;

  /**
   * To update the BusinessProfile
   * @param profile Profile object containing the fields to be changed
   */
  @Transactional
  public BusinessProfileUpdateRequest updateProfile(BusinessProfilePatchRequest profile) {
    BusinessProfileUpdateRequest businessProfileUpdateRequest =
        _businessProfileRequestService.createBusinessProfileRequest(profile, RequestType.UPDATE, new HashSet<>());
    logger.info("created profile request with id: " + businessProfileUpdateRequest.getRequestId());
    sendProfileUpdateRequest(businessProfileUpdateRequest);
    return businessProfileUpdateRequest;
  }

  /**
   * Update the BusinessProfile and validate it against the subscriptions
   * @param profile profileObject
   * @param subscriptions subscriptions against which we need to validate profile
   */
  @Transactional
  public String updateProfile(BusinessProfilePatchRequest profile, Set<String> subscriptions) {
    BusinessProfileUpdateRequest businessProfileUpdateRequest =
        _businessProfileRequestService.createBusinessProfileRequest(profile, RequestType.SUBSCRIBE, subscriptions);
    sendProfileUpdateRequest(businessProfileUpdateRequest);
    return businessProfileUpdateRequest.getRequestId();
  }

  /**
   * Update the BusinessProfile and validate it against the subscriptions
   * @param profile profileObject
   * @param subscriptions subscriptions against which we need to validate profile
   */
  @Transactional
  public String updateProfile(BusinessProfile profile, Set<String> subscriptions) {
    BusinessProfileUpdateRequest businessProfileUpdateRequest =
        _businessProfileRequestService.createBusinessProfileRequest(businessProfilePatchMapper.toPatchRequest(profile),
            RequestType.SUBSCRIBE, subscriptions);
    sendProfileUpdateRequest(businessProfileUpdateRequest);
    return businessProfileUpdateRequest.getRequestId();
  }

  /**
   * Create the draft businessProfile and send kafka message to validate the profile and make it active
   * @param profile profileObject
   * @return profileId
   */
  @Transactional
  @CachePut(value = "businessProfiles", key = "#profile.profileId")
  public BusinessProfile createProfileRequest(BusinessProfile profile) {
    BusinessProfileEntity businessProfileEntity = createBusinessProfileEntity(profile);
//    BusinessProfileUpdateRequest businessProfileUpdateRequest =
//        profileRequestService.createBusinessProfileRequest(profile, RequestType.CREATE, new HashSet<>());
//    sendProfileUpdateRequest(businessProfileUpdateRequest);
    return businessProfileMapper.toDto(businessProfileEntity);
  }

  /**
   * Delete businessProfile associated with given profileId
   * @param profileId profileId of the businessProfile
   */
  @CacheEvict(value = "businessProfiles", key = "#profileId")
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
  @Cacheable(value = "businessProfiles", key = "#profileId")
  public BusinessProfile getProfileById(String profileId) {
    BusinessProfileEntity profileEntity = profileRepository.getProfileById(profileId);
    if(profileEntity == null) {
      logger.warn("Business profile with id: " + profileId + " not found");
      throw new BusinessProfileNotFoundException("Business profile with id: " + profileId + " not found");
    }
    return businessProfileMapper.toDto(profileEntity);
  }

  /**
   * Create and save the draft profile
   * @param profile profileObject
   * @return profile Entity
   */
  private BusinessProfileEntity createBusinessProfileEntity(BusinessProfile profile) {
    BusinessProfileEntity profileEntity = businessProfileMapper.toEntity(profile);
    profileEntity.setStatus(ProfileStatus.DRAFT);
    return profileRepository.save(profileEntity);
  }

  /**
   * Update the business profile and activate it
   * @param profile profileObject
   * @return profile Entity
   */
  @CachePut(value = "businessProfiles", key = "#profile.profileId")
  public BusinessProfileEntity updateBusinessProfileEntity(BusinessProfile profile) {
    BusinessProfileEntity profileEntity = businessProfileMapper.toEntity(profile);
    profileEntity.setStatus(ProfileStatus.ACTIVE);
    return profileRepository.save(profileEntity);
  }

  /**
   * Send profile for async validations
   * @param request profile request Object
   */
  public void sendProfileUpdateRequest(BusinessProfileUpdateRequest request) {
    profileUpdateRequestProducer.sendProfileUpdateRequestWithKey(
        request.getProfileId(),
        request);
  }
}

