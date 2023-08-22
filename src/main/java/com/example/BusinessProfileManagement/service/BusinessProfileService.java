package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.cache.BusinessProfileCacheService;
import com.example.BusinessProfileManagement.factory.ProductValidationFactory;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileStatusResponse;
import com.example.BusinessProfileManagement.model.ProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.Product;
import com.example.BusinessProfileManagement.model.mapper.ProfileMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class BusinessProfileService {
  final BusinessProfileRepository profileRepository;
  final ProductValidationFactory productValidationFactory;
//  final ProfileMapper profileMapper;
  final BusinessProfileCacheService cacheService;

  @Autowired
  public BusinessProfileService(BusinessProfileRepository profileRepository,
      ProductValidationFactory productValidationFactory,
      BusinessProfileCacheService cacheService) {
    this.profileRepository = profileRepository;
    this.productValidationFactory = productValidationFactory;
//    this.profileMapper = profileMapper;
    this.cacheService = cacheService;
  }

//  @Async
//  public void requestProfileUpdate(BusinessProfile profile, Product product) {
//    // Mock validation with external product API
//    boolean isValid = mockValidation(profile, product);
//
//    // Update approval status
//    if (isValid) {
//      profile.getProductApprovals().put(product, ApprovalStatus.APPROVED);
//    } else {
//      profile.getProductApprovals().put(product, ApprovalStatus.REJECTED);
//    }
//  }

//  @Async
//  public a  CompletableFuture<Void> updateProfileAsync(BusinessProfile profile) {
//    for (Product product : Product.values()) {
//      requestProfileUpdate(profile, product);
//    }
//  }

  @Async
  public CompletableFuture<Void> updateProfileAsync(ProfileUpdateRequest request) {
    // 1. Save the updated profile to DynamoDB or your data store
    // ... (your code to update the profile)
//    BusinessProfile businessProfile = profileMapper.profileUpdateRequestToBusinessProfile(request, ApprovalStatus.IN_PROGRESS);
    BusinessProfile businessProfile = new BusinessProfile();
    saveProfile(businessProfile);
    List<CompletableFuture<ApprovalStatus>> validationTasks = new ArrayList<>();

    for (Product product : request.getSubscriptions()) {
      CompletableFuture<ApprovalStatus> validationTask = productValidationFactory
          .createValidationTask(product, request);
      validationTasks.add(validationTask);
    }
    // 3. Wait for all validation tasks to complete
    return CompletableFuture.allOf(validationTasks.toArray(new CompletableFuture[0]))
        .thenApplyAsync((approvalResult) -> {
          boolean allApproved = validationTasks.stream()
              .allMatch(task -> task.join() == ApprovalStatus.APPROVED);
          ApprovalStatus profileStatus = allApproved ? ApprovalStatus.APPROVED : ApprovalStatus.REJECTED;
          businessProfile.setApprovalStatus(profileStatus);
          saveProfile(businessProfile);
          //TODO: send email/notification about request status
          return null;
        });
  }

  private boolean mockValidation(BusinessProfile profile, Product product) {
    // Implement mock validation logic with the external product's API
    // Return true for successful validation, false for rejection (mocked)
    return true;
  }

  public BusinessProfile saveProfile(BusinessProfile profile) {
    return profileRepository.save(profile);
  }

  public void deleteProfile(String profileId) {
    profileRepository.delete(profileId);
  }

  public BusinessProfileStatusResponse getProfileById(String profileId) {
//    return profileRepository.getProfileById(profileId);
    BusinessProfile cachedProfileInProgress = cacheService.getProfileFromCache(profileId, ApprovalStatus.IN_PROGRESS);
    BusinessProfile cachedProfileApproved = cacheService.getProfileFromCache(profileId, ApprovalStatus.APPROVED);
    if(cachedProfileApproved == null) {
      cachedProfileApproved = profileRepository.findByProfileIdAndStatus(profileId, ApprovalStatus.APPROVED);
      if (cachedProfileApproved != null) {
        cacheService.storeProfile(profileId, ApprovalStatus.APPROVED, cachedProfileApproved);
      }
    }

    if(cachedProfileInProgress == null) {
      cachedProfileInProgress = profileRepository.findByProfileIdAndStatus(profileId, ApprovalStatus.IN_PROGRESS);
      if (cachedProfileInProgress != null) {
        cacheService.storeProfile(profileId, ApprovalStatus.IN_PROGRESS, cachedProfileInProgress);
      }
    }
    BusinessProfileStatusResponse businessProfileStatusResponse = new BusinessProfileStatusResponse();
    Map<ApprovalStatus,BusinessProfile> businessProfileMap  = new HashMap<>();
    if(cachedProfileInProgress!=null) {
      businessProfileMap.put(ApprovalStatus.IN_PROGRESS, cachedProfileInProgress);
    }
    businessProfileMap.put(ApprovalStatus.APPROVED, cachedProfileApproved);
    businessProfileStatusResponse.setApprovalStatusBusinessProfileMap(businessProfileMap);
    return businessProfileStatusResponse;
  }
}

