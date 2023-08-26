package com.example.BusinessProfileManagement.helper;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import java.util.UUID;


public class ProfileRequestHelper {

  public static BusinessProfileRequest createBusinessProfileRequest(BusinessProfile businessProfile, RequestType requestType) {
    BusinessProfileRequest businessProfileRequest = new BusinessProfileRequest();
    businessProfileRequest.setBusinessProfile(businessProfile);
    businessProfileRequest.setProfileId(businessProfile.getProfileId());
    businessProfileRequest.setRequestId(UUID.randomUUID().toString());
    businessProfileRequest.setStatus(ApprovalStatus.IN_PROGRESS);
    businessProfileRequest.setRequestType(requestType);
    return businessProfileRequest;
  }
}
