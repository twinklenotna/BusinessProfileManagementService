package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileRequestException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.helper.ProductValidationHelper;
import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchRequestMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestResponseMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestRepository;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BusinessProfileRequestServiceTest {
  private final String PROFILE_ID = "12346";

  @InjectMocks
  private BusinessProfileRequestService _businessProfileRequestService;

  @Mock
  private BusinessProfileRequestRepository businessProfileRequestRepository;

  @Mock
  private BusinessProfileService businessProfileService;

  @Mock
  private ProfileSubscriptionService profileSubscriptionService;

  @Mock
  BusinessProfileProductValidationService _businessProfileProductValidationService;
  @Mock
  BusinessProfileRequestMapper _businessProfileRequestMapper;
  @Mock
  BusinessProfileRequestResponseMapper _businessProfileRequestResponseMapper;
  @Mock
  BusinessProfileMapper _businessProfileMapper;

  @Mock
  BusinessProfilePatchRequestMapper _businessProfilePatchRequestMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }


  @Test
  public void testGetProfileRequestByprofileId() {

    when(businessProfileRequestRepository.findByprofileId(PROFILE_ID))
        .thenReturn(ProfileRequestHelper.createBusinessProfileEntityRequests(3, PROFILE_ID));
    when(_businessProfileRequestMapper.entityToDto(any(BusinessProfileRequestEntity.class)))
        .thenReturn(ProfileRequestHelper
            .createBusinessProfileRequest(ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID), RequestType.CREATE));

    List<BusinessProfileUpdateRequest> requests = _businessProfileRequestService.getProfileUpdateRequestsByprofileId(PROFILE_ID);

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
    when(_businessProfileProductValidationService.getRequestProductValidations(requestId))
        .thenReturn(ProductValidationHelper.createProfileRequestProductValidations(3, requestId, ApprovalStatus.APPROVED));
    when(_businessProfileRequestResponseMapper.entityToDto(businessProfileRequestEntity)).thenReturn(businessProfileRequestResponse);

    BusinessProfileRequestResponse request = _businessProfileRequestService.getProfileUpdateRequest(requestId);

    assertNotNull(request);
  }

  @Test
  public void testGetProfileRequestException() {
    String requestId = "12345";
    when(businessProfileRequestRepository.findByRequestId(requestId)).thenReturn(null);
    when(_businessProfileProductValidationService.getRequestProductValidations(requestId))
        .thenReturn(null);
    assertThrows(BusinessProfileRequestNotFoundException.class, () -> {
      _businessProfileRequestService.getProfileUpdateRequest(requestId);
    });
  }

  @Test
  public void testUpdateRequestStatus() {
    BusinessProfilePatchRequest businessProfile = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfileUpdateRequest request = ProfileRequestHelper
        .createBusinessProfileRequest(businessProfile, RequestType.UPDATE);
    BusinessProfileRequestEntity businessProfileRequestEntity =
        ProfileRequestHelper.createBusinessProfileRequestEntity(ProfileHelper.createBusinessProfileEntity(PROFILE_ID), RequestType.UPDATE);
    businessProfileRequestEntity.setStatus(ApprovalStatus.APPROVED);
    ApprovalStatus status = ApprovalStatus.APPROVED;

    when(businessProfileRequestRepository.saveAndUpdate(any())).thenReturn(businessProfileRequestEntity);

    _businessProfileRequestService.updateRequestStatus(request, status);

    verify(businessProfileRequestRepository, times(1)).saveAndUpdate(any());
  }

  @Test
  public void testUpdateRequestStatusException() {
    BusinessProfilePatchRequest businessProfile = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfileUpdateRequest request = ProfileRequestHelper
        .createBusinessProfileRequest(businessProfile, RequestType.UPDATE);

    when(businessProfileRequestRepository.saveAndUpdate(any())).thenThrow(new RuntimeException("exception while saving"));

    assertThrows(BusinessProfileRequestException.class, () -> {
      _businessProfileRequestService.updateRequestStatus(request, ApprovalStatus.APPROVED);
    });
  }


  @Test
  public void testCreateBusinessProfileRequest() {
    BusinessProfilePatchRequest businessProfile = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfileEntity businessProfileEntity = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);
    BusinessProfileUpdateRequest request = ProfileRequestHelper.createBusinessProfileRequest(businessProfile, RequestType.UPDATE);
    BusinessProfileRequestEntity businessProfileRequestEntity =
        ProfileRequestHelper.createBusinessProfileRequestEntity(ProfileHelper.createBusinessProfileEntity(PROFILE_ID), RequestType.UPDATE);

    when(businessProfileRequestRepository.saveAndUpdate(any())).thenReturn(businessProfileRequestEntity);
    when(_businessProfilePatchRequestMapper.toEntity(businessProfile)).thenReturn(businessProfileEntity);
    when(_businessProfileRequestMapper.entityToDto(businessProfileRequestEntity)).thenReturn(request);

    _businessProfileRequestService.createBusinessProfileRequest(businessProfile, RequestType.UPDATE, request.getSubscriptions());

    verify(businessProfileRequestRepository, times(1)).saveAndUpdate(any());
  }
}
