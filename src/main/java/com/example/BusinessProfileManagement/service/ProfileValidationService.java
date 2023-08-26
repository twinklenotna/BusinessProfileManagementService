package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.factory.ProductValidationFactory;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestProductValidationMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestProductValidationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
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
    List<CompletableFuture<BusinessProfileRequestProductValidation>> validationTasks = new ArrayList<>();
    for (String product : request.getSubscriptions()) {
      // Start a validation task for each product
      CompletableFuture<BusinessProfileRequestProductValidation> validationTask = validateRequest(product, request.getBusinessProfile(),
          request.getRequestId());
      validationTasks.add(validationTask);
    }
    CompletableFuture<Void> allOf = CompletableFuture.allOf(validationTasks.toArray(new CompletableFuture[0]));
    AtomicBoolean allApproved = new AtomicBoolean(true);
    allOf.thenRun(() -> {
      for (CompletableFuture<BusinessProfileRequestProductValidation> validationTask : validationTasks) {
        try {
          BusinessProfileRequestProductValidation validation = validationTask.get();
          if (validation.getStatus().equals(ApprovalStatus.FAILED)) {
            logger.error("Validation failed for requestId: "+ request);
            throw new BusinessProfileValidationException("Failed to validate request with requestId: " + request.getRequestId() +
                " with product: " + validation.getProductId());
          } else if (validation.getStatus().equals(ApprovalStatus.REJECTED)) {
            allApproved.set(false);
          }
        } catch (Exception e) {
          throw new BusinessProfileValidationException(e.getMessage(),e);
        }
      }
    });
    logger.debug("Validation completed for requestId: "+ request+ " with status: "+ allApproved.get());
    return allApproved.get();
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
