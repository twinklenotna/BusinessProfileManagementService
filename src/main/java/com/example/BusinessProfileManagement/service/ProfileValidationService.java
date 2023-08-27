package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.factory.ProductValidationFactory;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestProductValidationMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestProductValidationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProfileValidationService {
  Logger logger = LoggerFactory.getLogger(ProfileValidationService.class);
  final BusinessProfileRequestProductValidationRepository _businessProfileRequestProductValidationRepository;
  final ProductValidationFactory _productValidationFactory;
  final BusinessProfileService _businessProfileService;
  final ProfileSubscriptionService _profileSubscriptionService;
  final ProfileRequestService _profileRequestService;


  public ProfileValidationService(
      BusinessProfileRequestProductValidationRepository businessProfileRequestProductValidationRepository,
      ProductValidationFactory productValidationFactory, BusinessProfileService businessProfileService,
      ProfileSubscriptionService profileSubscriptionService, ProfileRequestService profileRequestService) {
    _businessProfileRequestProductValidationRepository = businessProfileRequestProductValidationRepository;
    _productValidationFactory = productValidationFactory;
    _businessProfileService = businessProfileService;
    _profileSubscriptionService = profileSubscriptionService;
    _profileRequestService = profileRequestService;
  }

  @Transactional
  public boolean validateRequest(BusinessProfileRequest request) {
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

  public List<BusinessProfileRequestProductValidation> getRequestProductValidations(String requestId) {
    List<BusinessProfileRequestProductValidationEntity> profileRequestProductValidations =
        _businessProfileRequestProductValidationRepository.findByRequestId(requestId);
    List<BusinessProfileRequestProductValidation> validations = new ArrayList<>();
    for(BusinessProfileRequestProductValidationEntity productValidation : profileRequestProductValidations) {
      validations.add(BusinessProfileRequestProductValidationMapper.INSTANCE.entityToDto(productValidation));
    }
    return validations;
  }

  public Set<String> getSuccessfulProductValidations(String requestId) {
    List<BusinessProfileRequestProductValidation> allProductValidations = getRequestProductValidations(requestId);
    return allProductValidations.stream()
        .filter(validateRequest -> !validateRequest.getStatus().equals(ApprovalStatus.FAILED))
        .map(validateRequest -> validateRequest.getProductId())
        .collect(
        Collectors.toSet());
  }

  private void enrichRequest(BusinessProfileRequest request) {
    BusinessProfile businessProfile = fetchBusinessProfileEntity(request.getProfileId(), request.getBusinessProfile());
    request.setBusinessProfile(businessProfile);
    fetchSubscriptions(request);
  }

  public CompletableFuture<BusinessProfileRequestProductValidation> validateRequest(String productId, BusinessProfile profile, String requestId) {
    return CompletableFuture.supplyAsync(() -> {
      BusinessProfileRequestProductValidation validation = _productValidationFactory
          .validateProfile(productId, profile);
      validation.setRequestId(requestId);
      _businessProfileRequestProductValidationRepository.saveAndFlush(BusinessProfileRequestProductValidationMapper.INSTANCE.dtoToEntity(validation));
      return validation;
    });
  }
  public BusinessProfile fetchBusinessProfileEntity(String profileId, BusinessProfile businessProfilePatchRequest) {
    BusinessProfile businessProfile = _businessProfileService.getProfileById(profileId);
    businessProfile.applyPatch(businessProfilePatchRequest);
    return businessProfile;
  }

  public void fetchSubscriptions(BusinessProfileRequest request) {
    if(request.getSubscriptions().size() == 0) {
      request.setSubscriptions(_profileSubscriptionService.getSubscriptions(request.getProfileId()));
    }
  }
}
