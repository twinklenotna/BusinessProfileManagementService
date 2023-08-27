package com.example.BusinessProfileManagement.helper;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
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

  public static BusinessProfileRequest createBusinessProfileRequest(BusinessProfile businessProfile, RequestType requestType) {
    BusinessProfileRequest businessProfileRequest = new BusinessProfileRequest();
    businessProfileRequest.setBusinessProfile(businessProfile);
    businessProfileRequest.setProfileId(businessProfile.getProfileId());
    businessProfileRequest.setRequestId(UUID.randomUUID().toString());
    businessProfileRequest.setStatus(ApprovalStatus.IN_PROGRESS);
    businessProfileRequest.setRequestType(requestType);
    Set<String> subscriptions = new HashSet<>();
    subscriptions.add("Subscription1");
    subscriptions.add("Subscription2");
    businessProfileRequest.setSubscriptions(subscriptions);
    return businessProfileRequest;
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
    return businessProfileRequest;
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

  public static List<BusinessProfileRequest> createBusinessProfileRequests(int requests, String profileId) {
    List<BusinessProfileRequest> businessProfileRequests = new ArrayList<>();
    for (int i = 0; i < requests; i++) {
      businessProfileRequests.add(
          createBusinessProfileRequest(ProfileHelper.createBusinessProfile(profileId), RequestType.UPDATE));
    }
    return businessProfileRequests;
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
