package com.example.BusinessProfileManagement.client;

import com.example.BusinessProfileManagement.config.ProductUrlMapConfig;
import com.example.BusinessProfileManagement.exception.BusinessProfileValidationClientException;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@Component
public class DefaultProductClient implements ProductClient {
  private final ProductUrlMapConfig productUrlMapConfig;
  private final RestTemplate restTemplate;

  @Autowired
  public DefaultProductClient(ProductUrlMapConfig productUrlMapConfig, RestTemplate restTemplate) {
    this.productUrlMapConfig = productUrlMapConfig;
    this.restTemplate = restTemplate;
  }

//  @HystrixCommand(fallbackMethod = "defaultApprovalStatus")
  @Override
  public BusinessProfileRequestProductValidation getApproval(String product, BusinessProfile profile) {
    String baseUrl = productUrlMapConfig.getUrl().getOrDefault(product, "defaultUrlForUnknownProduct");
    String validateUrl = baseUrl + "/validate";

    BusinessProfileRequestProductValidation businessProfileRequestProductValidation = new BusinessProfileRequestProductValidation();
    businessProfileRequestProductValidation.setProductId(product);

    try {
      ResponseEntity<ApprovalStatus> response = sendRequest(profile, validateUrl);
      businessProfileRequestProductValidation.setStatus(response.getBody());
      return businessProfileRequestProductValidation;
    } catch (BusinessProfileValidationClientException ex) {
      // Log exception
      businessProfileRequestProductValidation.setStatus(ApprovalStatus.FAILED);
      return businessProfileRequestProductValidation;
    }
  }

  private ResponseEntity<ApprovalStatus> sendRequest(BusinessProfile profile, String validateUrl) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<BusinessProfile> requestEntity = new HttpEntity<>(profile, headers);
    return restTemplate.exchange(validateUrl, HttpMethod.POST, requestEntity, ApprovalStatus.class);
  }
}
