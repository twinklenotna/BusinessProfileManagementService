package com.example.BusinessProfileManagement.model;

import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProfileUpdateRequest {
  private String profileId;
  private String requestId;
  private ApprovalStatus status;
  private RequestType requestType;
  private String comments;
  private BusinessProfile businessProfile;
  private Set<String> subscriptions;
}
