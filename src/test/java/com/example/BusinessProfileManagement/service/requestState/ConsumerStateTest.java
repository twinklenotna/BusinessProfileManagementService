package com.example.BusinessProfileManagement.service.requestState;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchMapper;
import com.example.BusinessProfileManagement.service.BusinessProfileRequestService;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import com.example.BusinessProfileManagement.service.ProfileSubscriptionService;
import com.example.BusinessProfileManagement.service.ProfileValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class ConsumerStateTest {
  private final String PROFILE_ID = "1234";
  @Mock
  BusinessProfileService _businessProfileService;
  @Mock
  BusinessProfileRequestService _businessProfileRequestService;
  @Mock
  ProfileValidationService _profileValidationService;
  @Mock ProfileSubscriptionService _profileSubscriptionService;
  @Mock
  BusinessProfilePatchMapper _businessProfilePatchMapper;
  @Mock
  RejectedState _rejectedState;
  @Mock
  ApprovedState _approvedState;
  @Mock
  InProgressState _inProgressState;
  @Mock
  FailedState _failedState;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testApprovalFLow() {
    BusinessProfileRequestContext context =
        new BusinessProfileRequestContext(
            new ApprovedState(
                _businessProfileService, _businessProfileRequestService, _profileSubscriptionService, _businessProfilePatchMapper),
            new FailedState(),
            new RejectedState(_businessProfileRequestService),
            new InProgressState(_profileValidationService));

    BusinessProfile businessProfile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfilePatchRequest businessProfilePatch = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfileUpdateRequest request =
        ProfileRequestHelper.createBusinessProfileRequest(businessProfilePatch, RequestType.SUBSCRIBE);
    ProfileSubscription profileSubscription = new ProfileSubscription(PROFILE_ID, request.getSubscriptions());

    when(_profileValidationService.validateRequest(request)).thenReturn(true);
    context.processRequest(request);

    verify(_profileValidationService, times(1)).validateRequest(eq(request));
    verify(_profileSubscriptionService, times(1)).addSubscriptions(eq(PROFILE_ID), eq(request.getSubscriptions()));
    verify(_businessProfileRequestService, times(1)).updateBusinessProfileRequestEntity(any());
    verify(_businessProfileService, times(1))
        .updateBusinessProfileEntity(any());
  }

  @Test
  public void testRejectionFLow() {
    BusinessProfileRequestContext context =
        new BusinessProfileRequestContext(
            new ApprovedState(
                _businessProfileService, _businessProfileRequestService, _profileSubscriptionService, _businessProfilePatchMapper ),
            new FailedState(),
            new RejectedState(_businessProfileRequestService),
            new InProgressState(_profileValidationService));

    BusinessProfilePatchRequest businessProfilePatch = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfileUpdateRequest request =
        ProfileRequestHelper.createBusinessProfileRequest(businessProfilePatch, RequestType.SUBSCRIBE);

    when(_profileValidationService.validateRequest(request)).thenReturn(false);
    context.processRequest(request);
    verify(_profileValidationService, times(1)).validateRequest(eq(request));
    verify(_profileSubscriptionService, times(0)).subscribe(any());
    verify(_businessProfileRequestService, times(1)).updateBusinessProfileRequestEntity(any());
    verify(_businessProfileService, times(0)).updateBusinessProfileEntity(any());
  }

  @Test
  public void testFailureFLow() {
    BusinessProfileRequestContext context =
        new BusinessProfileRequestContext(
            new ApprovedState(
                _businessProfileService, _businessProfileRequestService, _profileSubscriptionService, _businessProfilePatchMapper),
            new FailedState(),
            new RejectedState(_businessProfileRequestService),
            new InProgressState(_profileValidationService));

    BusinessProfilePatchRequest businessProfilePatch = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfileUpdateRequest request =
        ProfileRequestHelper.createBusinessProfileRequest(businessProfilePatch, RequestType.SUBSCRIBE);

    when(_profileValidationService.validateRequest(request)).thenThrow(new BusinessProfileValidationException("validation failed"));
    assertThrows(BusinessProfileValidationException.class, () -> {
      context.processRequest(request);
    });
    verify(_profileValidationService, times(1)).validateRequest(eq(request));
    verify(_profileSubscriptionService, times(0)).subscribe(any());
    verify(_businessProfileRequestService, times(0)).updateBusinessProfileRequestEntity(any());
    verify(_businessProfileService, times(0)).updateBusinessProfileEntity(any());
  }
}

