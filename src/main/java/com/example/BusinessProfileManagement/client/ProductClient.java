package com.example.BusinessProfileManagement.client;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import org.springframework.stereotype.Component;


@Component
public interface ProductClient {
  BusinessProfileRequestProductValidation getApproval(String product, BusinessProfile profileId);
  default BusinessProfileRequestProductValidation defaultApprovalStatus(String product, BusinessProfile profile, Throwable throwable) {
    BusinessProfileRequestProductValidation fallbackValidation = new BusinessProfileRequestProductValidation();
    fallbackValidation.setProductId(product);
    fallbackValidation.setStatus(ApprovalStatus.FAILED);
    return fallbackValidation;
  }
}
