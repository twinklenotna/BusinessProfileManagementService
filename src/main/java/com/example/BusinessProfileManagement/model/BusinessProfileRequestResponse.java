package com.example.BusinessProfileManagement.model;

import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import java.util.List;
import lombok.Data;

@Data
public class BusinessProfileRequestResponse {
  private String profileId;
  private String requestId;
  private ApprovalStatus status;
  private RequestType requestType;
  private String comments;
  private BusinessProfile businessProfile;
  private List<BusinessProfileRequestProductValidation> subscriptionValidations;
}
