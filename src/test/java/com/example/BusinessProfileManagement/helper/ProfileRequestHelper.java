package com.example.BusinessProfileManagement.helper;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class ProfileRequestHelper {

  public static BusinessProfileUpdateRequest createBusinessProfileRequest(String profileId) {
    return createBusinessProfileRequest(ProfileHelper.createBusinessProfile(profileId), RequestType.UPDATE);
  }

  public static BusinessProfileUpdateRequest createBusinessProfileRequest(String profileId, RequestType request) {
    return createBusinessProfileRequest(ProfileHelper.createBusinessProfile(profileId), request);
  }

  public static BusinessProfileUpdateRequest createBusinessProfileRequest(BusinessProfile businessProfile, RequestType requestType) {
    BusinessProfileUpdateRequest businessProfileUpdateRequest = new BusinessProfileUpdateRequest();
    businessProfileUpdateRequest.setBusinessProfile(businessProfile);
    businessProfileUpdateRequest.setProfileId(businessProfile.getProfileId());
    businessProfileUpdateRequest.setRequestId(UUID.randomUUID().toString());
    businessProfileUpdateRequest.setStatus(ApprovalStatus.IN_PROGRESS);
    businessProfileUpdateRequest.setRequestType(requestType);
    Set<String> subscriptions = new HashSet<>();
    subscriptions.add("Subscription1");
    subscriptions.add("Subscription2");
    businessProfileUpdateRequest.setSubscriptions(subscriptions);
    return businessProfileUpdateRequest;
  }

  public static BusinessProfileRequestResponse createBusinessProfileRequestResponse(String profileId) {
    return createBusinessProfileRequestResponse(ProfileHelper.createBusinessProfile(profileId), RequestType.UPDATE);
  }

  public static BusinessProfileRequestResponse createBusinessProfileRequestResponse(BusinessProfile businessProfile, RequestType requestType) {
    BusinessProfileRequestResponse businessProfileRequest = new BusinessProfileRequestResponse();
    businessProfileRequest.setBusinessProfile(businessProfile);
    businessProfileRequest.setProfileId(businessProfile.getProfileId());
    businessProfileRequest.setRequestId(UUID.randomUUID().toString());
    businessProfileRequest.setStatus(ApprovalStatus.IN_PROGRESS);
    businessProfileRequest.setRequestType(requestType);
    businessProfileRequest.setSubscriptionValidations(ProductValidationHelper
        .createProfileRequestProductValidations(2, businessProfileRequest.getRequestId(), ApprovalStatus.APPROVED));
    businessProfileRequest.setComments("comments");
    return businessProfileRequest;
  }

  public static BusinessProfileRequestEntity createBusinessProfileRequestEntity(
      String profileId) {
    return createBusinessProfileRequestEntity(ProfileHelper.createBusinessProfileEntity(profileId),
        RequestType.UPDATE, ApprovalStatus.IN_PROGRESS);
  }

  public static BusinessProfileRequestEntity createBusinessProfileRequestEntity(
      BusinessProfileEntity businessProfile, RequestType requestType) {
    return createBusinessProfileRequestEntity(businessProfile, requestType, ApprovalStatus.IN_PROGRESS);
  }

  public static BusinessProfileRequestEntity createBusinessProfileRequestEntity(
      BusinessProfileEntity businessProfile, RequestType requestType, ApprovalStatus status) {
    BusinessProfileRequestEntity businessProfileRequest = new BusinessProfileRequestEntity();
    businessProfileRequest.setBusinessProfile(businessProfile);
    businessProfileRequest.setProfileId(businessProfile.getProfileId());
    businessProfileRequest.setRequestId(UUID.randomUUID().toString());
    businessProfileRequest.setStatus(status);
    businessProfileRequest.setRequestType(requestType);
    Set<String> subscriptions = new HashSet<>();
    subscriptions.add("Subscription1");
    subscriptions.add("Subscription2");
    businessProfileRequest.setSubscriptions(subscriptions);
    return businessProfileRequest;
  }

  public static List<BusinessProfileUpdateRequest> createBusinessProfileRequests(int requests, String profileId) {
    List<BusinessProfileUpdateRequest> businessProfileUpdateRequests = new ArrayList<>();
    for (int i = 0; i < requests; i++) {
      businessProfileUpdateRequests.add(
          createBusinessProfileRequest(ProfileHelper.createBusinessProfile(profileId), RequestType.UPDATE));
    }
    return businessProfileUpdateRequests;
  }

  public static List<BusinessProfileRequestEntity> createBusinessProfileEntityRequests(int requests, String profileId) {
    List<BusinessProfileRequestEntity> businessProfileRequests = new ArrayList<>();
    for (int i = 0; i < requests; i++) {
      businessProfileRequests.add(
          createBusinessProfileRequestEntity(ProfileHelper.createBusinessProfileEntity(profileId), RequestType.UPDATE));
    }
    return businessProfileRequests;
  }

}
