package com.example.BusinessProfileManagement.client;

import com.example.BusinessProfileManagement.config.ProductUrlMapConfig;
import com.example.BusinessProfileManagement.model.ProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.Product;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;


@Component
public class DefaultProductClient implements ProductClient{
  private final ProductUrlMapConfig productUrlMapConfig;
  private final RestTemplate restTemplate;


  @Autowired
  public DefaultProductClient(ProductUrlMapConfig productUrlMapConfig,
      RestTemplate restTemplate) {
    this.productUrlMapConfig = productUrlMapConfig;
    this.restTemplate = restTemplate;
  }

  @Override
  public CompletableFuture<ApprovalStatus> getApproval(Product product, ProfileUpdateRequest profile) {
    String baseUrl = productUrlMapConfig.getUrl().getOrDefault(product, "defaultUrlForUnknownProduct");
    String validateUrl = baseUrl + "/validate";

    // Create headers
//    sendRequest(profile, validateUrl);
    if(profile.getProfileId().contains("twinkle")) {
      return CompletableFuture.completedFuture(ApprovalStatus.APPROVED);
    }
    return CompletableFuture.completedFuture(ApprovalStatus.REJECTED);

//    return CompletableFuture.completedFuture(approvalStatus);
  }

  private void sendRequest(ProfileUpdateRequest profile, String validateUrl) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    // Create the request entity with the ProfileUpdateRequest as the request body
    HttpEntity<ProfileUpdateRequest> requestEntity = new HttpEntity<>(profile, headers);

    // Make an HTTP POST request to the validate endpoint
    ResponseEntity<ApprovalStatus> responseEntity = restTemplate.exchange(validateUrl,
        HttpMethod.POST,
        requestEntity,
        ApprovalStatus.class
    );

    // Get the response body, which contains the approval status
    ApprovalStatus approvalStatus = responseEntity.getBody();
  }
}
