package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.factory.ProductValidationFactory;
import com.example.BusinessProfileManagement.helper.ProductValidationHelper;
import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.kafka.ProfileUpdateRequestProducer;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestResponseMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileRequestServiceTest {
  private final String PROFILE_ID = "12346";

  @InjectMocks
  private ProfileRequestService profileRequestService;

  @Mock
  private BusinessProfileRequestRepository businessProfileRequestRepository;

  @Mock
  private BusinessProfileService businessProfileService;

  @Mock
  private ProfileSubscriptionService profileSubscriptionService;

  @Mock ProfileProductValidationService _profileProductValidationService;
  @Mock
  BusinessProfileRequestMapper _businessProfileRequestMapper;
  @Mock
  BusinessProfileRequestResponseMapper _businessProfileRequestResponseMapper;
  @Mock
  BusinessProfileMapper _businessProfileMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }


  @Test
  public void testGetProfileRequestByProfileId() {

    when(businessProfileRequestRepository.findByProfileId(PROFILE_ID))
        .thenReturn(ProfileRequestHelper.createBusinessProfileEntityRequests(3, PROFILE_ID));
    when(_businessProfileRequestMapper.entityToDto(any(BusinessProfileRequestEntity.class)))
        .thenReturn(ProfileRequestHelper
            .createBusinessProfileRequest(ProfileHelper.createBusinessProfile(PROFILE_ID), RequestType.CREATE));

    List<BusinessProfileRequest> requests = profileRequestService.getProfileRequestByProfileId(PROFILE_ID);

    assertNotNull(requests);
    assertEquals(requests.size(), 3);
  }

  @Test
  public void testGetProfileRequest() {
    BusinessProfileRequestEntity businessProfileRequestEntity =
        ProfileRequestHelper.createBusinessProfileRequestEntity(ProfileHelper.createBusinessProfileEntity(PROFILE_ID), RequestType.UPDATE);
    String requestId = businessProfileRequestEntity.getRequestId();
    BusinessProfileRequestResponse businessProfileRequestResponse = ProfileRequestHelper
        .createBusinessProfileRequestResponse(ProfileHelper.createBusinessProfile(PROFILE_ID), RequestType.UPDATE);
    businessProfileRequestResponse.setRequestId(requestId);

    when(businessProfileRequestRepository.findByRequestId(requestId)).thenReturn(businessProfileRequestEntity);
    when(_profileProductValidationService.getRequestProductValidations(requestId))
        .thenReturn(ProductValidationHelper.createProfileRequestProductValidations(3, requestId, ApprovalStatus.APPROVED));
    when(_businessProfileRequestResponseMapper.entityToDto(businessProfileRequestEntity)).thenReturn(businessProfileRequestResponse);

    BusinessProfileRequestResponse request = profileRequestService.getProfileRequest(requestId);

    assertNotNull(request);
  }

  @Test
  public void testGetProfileRequestException() {
    String requestId = "12345";
    when(businessProfileRequestRepository.findByRequestId(requestId)).thenReturn(null);
    when(_profileProductValidationService.getRequestProductValidations(requestId))
        .thenReturn(null);
    assertThrows(BusinessProfileRequestNotFoundException.class, () -> {
      profileRequestService.getProfileRequest(requestId);
    });
  }

  @Test
  public void testUpdateRequestStatus() {
    BusinessProfile businessProfile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileRequest request = ProfileRequestHelper.createBusinessProfileRequest(businessProfile, RequestType.UPDATE);
    BusinessProfileRequestEntity businessProfileRequestEntity =
        ProfileRequestHelper.createBusinessProfileRequestEntity(ProfileHelper.createBusinessProfileEntity(PROFILE_ID), RequestType.UPDATE);
    businessProfileRequestEntity.setStatus(ApprovalStatus.APPROVED);
    ApprovalStatus status = ApprovalStatus.APPROVED;

    when(businessProfileRequestRepository.saveAndUpdate(any())).thenReturn(businessProfileRequestEntity);

    profileRequestService.updateRequestStatus(request, status);

    verify(businessProfileRequestRepository, times(1)).saveAndUpdate(any());
  }

  @Test
  public void testUpdateRequestStatusException() {
    BusinessProfile businessProfile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileRequest request = ProfileRequestHelper.createBusinessProfileRequest(businessProfile, RequestType.UPDATE);

    when(businessProfileRequestRepository.saveAndUpdate(any())).thenThrow(new RuntimeException("exception while saving"));

    assertThrows(BusinessProfileRequestException.class, () -> {
      profileRequestService.updateRequestStatus(request, ApprovalStatus.APPROVED);
    });
  }


  @Test
  public void testCreateBusinessProfileRequest() {
    BusinessProfile businessProfile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileEntity businessProfileEntity = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);
    BusinessProfileRequest request = ProfileRequestHelper.createBusinessProfileRequest(businessProfile, RequestType.UPDATE);
    BusinessProfileRequestEntity businessProfileRequestEntity =
        ProfileRequestHelper.createBusinessProfileRequestEntity(ProfileHelper.createBusinessProfileEntity(PROFILE_ID), RequestType.UPDATE);

    when(businessProfileRequestRepository.saveAndUpdate(any())).thenReturn(businessProfileRequestEntity);
    when(_businessProfileMapper.dtoToEntity(businessProfile)).thenReturn(businessProfileEntity);
    when(_businessProfileRequestMapper.entityToDto(businessProfileRequestEntity)).thenReturn(request);

    profileRequestService.createBusinessProfileRequest(businessProfile, RequestType.UPDATE, request.getSubscriptions());

    verify(businessProfileRequestRepository, times(1)).saveAndUpdate(any());
  }
}
