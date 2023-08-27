package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.factory.ProductValidationFactory;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestProductValidationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileValidationService {
  Logger logger = LoggerFactory.getLogger(ProfileValidationService.class);
  final BusinessProfileRequestProductValidationRepository _businessProfileRequestProductValidationRepository;
  private final ProductValidationFactory productValidationFactory;
  private final BusinessProfileService _businessProfileService;
  private final ProfileSubscriptionService _profileSubscriptionService;
  private final ProfileRequestService profileRequestService;
  private final ProfileProductValidationService profileProductValidationService;


  @Transactional
  public boolean validateRequest(BusinessProfileUpdateRequest request) {
    logger.debug("Started Validation for requestId: "+ request);
    enrichRequest(request);
    Set<String> subscriptions = request.getSubscriptions();
    subscriptions.removeAll(getSuccessfulProductValidations(request.getRequestId()));
    List<CompletableFuture<BusinessProfileRequestProductValidation>> validationTasks = new ArrayList<>();

    for (String product : subscriptions) {
      // Start a validation task for each product
      CompletableFuture<BusinessProfileRequestProductValidation> validationTask = validateRequest(product, request.getBusinessProfile(),
          request.getRequestId());
      validationTasks.add(validationTask);
    }

    AtomicBoolean allApproved = new AtomicBoolean(true);
    CompletableFuture<Void> allOf = CompletableFuture.allOf(validationTasks.toArray(new CompletableFuture[0]));
    try {
      allOf.join();
      validationTasks.forEach(task -> {
        if (task.isCompletedExceptionally()) {
          task.join();
        }
      });

    } catch (CompletionException ex) {
      throw new BusinessProfileValidationException("Validation failed", ex);
    }

    for (CompletableFuture<BusinessProfileRequestProductValidation> validationTask : validationTasks) {
      BusinessProfileRequestProductValidation validation = validationTask.join();
      if (validation.getStatus().equals(ApprovalStatus.FAILED)) {
        logger.error("Validation failed for requestId: " + request);
        throw new BusinessProfileValidationException("Failed to validate request with requestId: " +
            request.getRequestId() + " with product: " + validation.getProductId());
      } else if (validation.getStatus().equals(ApprovalStatus.REJECTED)) {
        allApproved.set(false);
      }
    }
    logger.debug("Validation completed for requestId: "+ request+ " with status: "+ allApproved.get());
    return allApproved.get();
  }



  public Set<String> getSuccessfulProductValidations(String requestId) {
    List<BusinessProfileRequestProductValidation> allProductValidations =
        profileProductValidationService.getRequestProductValidations(requestId);
    return allProductValidations.stream()
        .filter(validateRequest -> !validateRequest.getStatus().equals(ApprovalStatus.FAILED))
        .map(validateRequest -> validateRequest.getProductId())
        .collect(
        Collectors.toSet());
  }

  private void enrichRequest(BusinessProfileUpdateRequest request) {
    BusinessProfile businessProfile = fetchBusinessProfileEntity(request.getProfileId(), request.getBusinessProfile());
    request.setBusinessProfile(businessProfile);
    fetchSubscriptions(request);
  }

  public CompletableFuture<BusinessProfileRequestProductValidation> validateRequest(String productId, BusinessProfile profile, String requestId) {
    return CompletableFuture.supplyAsync(() -> {
      BusinessProfileRequestProductValidation validation = productValidationFactory
          .validateProfile(productId, profile);
      validation.setRequestId(requestId);
      profileProductValidationService.saveBusinessProfileRequestProductValidation(validation);
      return validation;
    });
  }



  public BusinessProfile fetchBusinessProfileEntity(String profileId, BusinessProfile businessProfilePatchRequest) {
    BusinessProfile businessProfile = _businessProfileService.getProfileById(profileId);
    businessProfile.applyPatch(businessProfilePatchRequest);
    return businessProfile;
  }

  public void fetchSubscriptions(BusinessProfileUpdateRequest request) {
    if(request.getSubscriptions().size() == 0) {
      request.setSubscriptions(_profileSubscriptionService.getSubscriptions(request.getProfileId()));
    }
  }
}
