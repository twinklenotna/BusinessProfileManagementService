package com.example.BusinessProfileManagement.client;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Component;

@Component
public class QBOProductClient implements ProductClient{
  @Override
//  @HystrixCommand(fallbackMethod = "defaultApprovalStatus")
  public BusinessProfileRequestProductValidation getApproval(String product, BusinessProfile profileId) {
    BusinessProfileRequestProductValidation businessProfileRequestProductValidation = new BusinessProfileRequestProductValidation();
    businessProfileRequestProductValidation.setProductId(product);
    businessProfileRequestProductValidation.setStatus(ApprovalStatus.APPROVED);
    return businessProfileRequestProductValidation;
  }
}
