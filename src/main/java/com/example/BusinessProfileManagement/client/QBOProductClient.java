package com.example.BusinessProfileManagement.client;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;

@Component
public class QBOProductClient implements ProductClient{
  @Override
  @CircuitBreaker(name = "defaultApprovalStatus", fallbackMethod = "defaultApprovalStatus")
  public BusinessProfileRequestProductValidation getApproval(String product, BusinessProfile profileId) {
    BusinessProfileRequestProductValidation businessProfileRequestProductValidation = new BusinessProfileRequestProductValidation();
    businessProfileRequestProductValidation.setProductId(product);
    businessProfileRequestProductValidation.setStatus(ApprovalStatus.APPROVED);
    return businessProfileRequestProductValidation;
  }
}
