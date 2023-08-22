package com.example.BusinessProfileManagement.model;

import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import java.util.Map;
import lombok.Data;


@Data
public class BusinessProfileStatusResponse {
  Map<ApprovalStatus, BusinessProfile> approvalStatusBusinessProfileMap;
}
