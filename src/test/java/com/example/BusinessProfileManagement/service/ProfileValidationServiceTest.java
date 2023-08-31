package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.client.ProductClient;
import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.factory.ProductValidationFactory;
import com.example.BusinessProfileManagement.helper.ProductValidationHelper;
import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchRequestMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestProductValidationRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileValidationServiceTest {
  private final String PROFILE_ID = "12346";

  @InjectMocks
  private ProfileValidationService profileValidationService;

  @Mock
  private BusinessProfileRequestProductValidationRepository businessProfileRequestProductValidationRepository;

  @Mock
  private ProductValidationFactory productValidationFactory;

  @Mock
  private BusinessProfileService businessProfileService;

  @Mock
  private ProfileSubscriptionService profileSubscriptionService;

  @Mock
  private BusinessProfileRequestService _businessProfileRequestService;

  @Mock
  private BusinessProfileProductValidationService _businessProfileProductValidationService;

  @Mock
  private BusinessProfilePatchRequestMapper _businessProfilePatchRequestMapper;
  @Mock
  BusinessProfilePatchMapper _businessProfilePatchMapper;

  @Mock
  ProductClient _productClient;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testApproveRequest_AllProductsApproved() {
    BusinessProfilePatchRequest profilePatch = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileEntity profileEntity = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);
    BusinessProfileUpdateRequest businessProfileUpdateRequest =
        ProfileRequestHelper.createBusinessProfileRequest(profilePatch, RequestType.CREATE);
    BusinessProfileRequestEntity businessProfileRequestEntity =
        ProfileRequestHelper.createBusinessProfileRequestEntity(profileEntity, RequestType.CREATE, ApprovalStatus.APPROVED);
    businessProfileRequestEntity.setRequestId(businessProfileUpdateRequest.getRequestId());
    BusinessProfileRequestProductValidation businessProfileRequestProductValidation =
        ProductValidationHelper
            .createProfileRequestProductValidation(businessProfileUpdateRequest.getRequestId(), ApprovalStatus.APPROVED);
    BusinessProfileRequestProductValidationEntity businessProfileRequestProductValidationEntity =
        ProductValidationHelper
            .createProfileRequestProductValidationEntity(businessProfileUpdateRequest.getRequestId(), ApprovalStatus.APPROVED);
    List<BusinessProfileRequestProductValidation> businessProfileRequestProductValidations =
        ProductValidationHelper
            .createProfileRequestProductValidations(3, businessProfileUpdateRequest.getRequestId(), ApprovalStatus.APPROVED);


    when(businessProfileService.getProfileById(any())).thenReturn(profile);
    when(profileSubscriptionService.getSubscriptions(any())).thenReturn(new HashSet<>(
        Arrays.asList("product1", "product2")));
    when(productValidationFactory.getClient(any())).thenReturn(_productClient);
    when(_productClient.getApproval(any(),any())).thenReturn(businessProfileRequestProductValidation);
    when(businessProfileRequestProductValidationRepository.saveAndFlush(any())).thenReturn(businessProfileRequestProductValidationEntity);
    when(_businessProfileRequestService.updateBusinessProfileRequestEntity(any())).thenReturn(businessProfileRequestEntity);
    when(_businessProfileProductValidationService.getRequestProductValidations(businessProfileUpdateRequest.getRequestId()))
        .thenReturn(businessProfileRequestProductValidations);
    when(_businessProfileProductValidationService.saveBusinessProfileRequestProductValidation(any()))
        .thenReturn(businessProfileRequestProductValidations.get(0));
    when(_businessProfilePatchMapper.toPatchRequest(any())).thenReturn(profilePatch);

    boolean requestValidated = profileValidationService.validateRequest(businessProfileUpdateRequest);

    assertTrue(requestValidated);

  }

  @Test
  public void testApproveRequest_SomeProductsRejected() {
    BusinessProfilePatchRequest profilePatch = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileEntity profileEntity = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);
    BusinessProfileUpdateRequest businessProfileUpdateRequest =
        ProfileRequestHelper.createBusinessProfileRequest(profilePatch, RequestType.CREATE);
    BusinessProfileRequestEntity businessProfileRequestEntity =
        ProfileRequestHelper.createBusinessProfileRequestEntity(profileEntity, RequestType.CREATE, ApprovalStatus.APPROVED);
    businessProfileRequestEntity.setRequestId(businessProfileUpdateRequest.getRequestId());
    BusinessProfileRequestProductValidation businessProfileRequestProductValidation =
        ProductValidationHelper
            .createProfileRequestProductValidation(businessProfileUpdateRequest.getRequestId(), ApprovalStatus.REJECTED);
    BusinessProfileRequestProductValidationEntity businessProfileRequestProductValidationEntity =
        ProductValidationHelper
            .createProfileRequestProductValidationEntity(businessProfileUpdateRequest.getRequestId(), ApprovalStatus.APPROVED);

    when(businessProfileService.getProfileById(any())).thenReturn(profile);
    when(profileSubscriptionService.getSubscriptions(any())).thenReturn(new HashSet<>(
        Arrays.asList("product1", "product2")));
    when(productValidationFactory.getClient(any())).thenReturn(_productClient);
    when(_productClient.getApproval(any(),any())).thenReturn(businessProfileRequestProductValidation);
    when(businessProfileRequestProductValidationRepository.saveAndFlush(any())).thenReturn(businessProfileRequestProductValidationEntity);
    when(_businessProfileRequestService.updateBusinessProfileRequestEntity(any())).thenReturn(businessProfileRequestEntity);
    when(_businessProfileProductValidationService.getRequestProductValidations(businessProfileUpdateRequest.getRequestId()))
        .thenReturn(ProductValidationHelper.createProfileRequestProductValidations(3, businessProfileUpdateRequest.getRequestId(), ApprovalStatus.APPROVED));
    when(_businessProfileProductValidationService.saveBusinessProfileRequestProductValidation(any()))
        .thenReturn(businessProfileRequestProductValidation);
    when(_businessProfilePatchMapper.toPatchRequest(any())).thenReturn(profilePatch);


    boolean requestValidated = profileValidationService.validateRequest(businessProfileUpdateRequest);

    assertEquals(requestValidated, false);
  }

  @Test
  public void testApproveRequest_ValidationFails() {
    BusinessProfilePatchRequest profilePatch = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileEntity profileEntity = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);
    BusinessProfileUpdateRequest businessProfileUpdateRequest =
        ProfileRequestHelper.createBusinessProfileRequest(profilePatch, RequestType.CREATE);
    BusinessProfileRequestEntity businessProfileRequestEntity =
        ProfileRequestHelper.createBusinessProfileRequestEntity(profileEntity, RequestType.CREATE, ApprovalStatus.APPROVED);
    businessProfileRequestEntity.setRequestId(businessProfileUpdateRequest.getRequestId());
    BusinessProfileRequestProductValidation businessProfileRequestProductValidation =
        ProductValidationHelper
            .createProfileRequestProductValidation(businessProfileUpdateRequest.getRequestId(), ApprovalStatus.FAILED);
    BusinessProfileRequestProductValidationEntity businessProfileRequestProductValidationEntity =
        ProductValidationHelper
            .createProfileRequestProductValidationEntity(businessProfileUpdateRequest.getRequestId(), ApprovalStatus.APPROVED);

    when(_businessProfileProductValidationService.saveBusinessProfileRequestProductValidation(any()))
        .thenReturn(businessProfileRequestProductValidation);
    when(businessProfileService.getProfileById(any())).thenReturn(profile);
    when(profileSubscriptionService.getSubscriptions(any())).thenReturn(new HashSet<>(
        Arrays.asList("product1", "product2")));
    when(productValidationFactory.getClient(any())).thenReturn(_productClient);
    when(_productClient.getApproval(any(),any())).thenReturn(businessProfileRequestProductValidation);
    when(businessProfileRequestProductValidationRepository.saveAndFlush(any())).thenReturn(businessProfileRequestProductValidationEntity);
    when(_businessProfileRequestService.updateBusinessProfileRequestEntity(any())).thenReturn(businessProfileRequestEntity);
    when(_businessProfileProductValidationService.getRequestProductValidations(businessProfileUpdateRequest.getRequestId()))
        .thenReturn(ProductValidationHelper.createProfileRequestProductValidations(3, businessProfileUpdateRequest.getRequestId(), ApprovalStatus.APPROVED));


    assertThrows(BusinessProfileValidationException.class, () -> {
      profileValidationService.validateRequest(businessProfileUpdateRequest);
    });
  }

}

