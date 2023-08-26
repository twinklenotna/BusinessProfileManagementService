package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.factory.ProductValidationFactory;
import com.example.BusinessProfileManagement.kafka.ProfileUpdateRequestProducer;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
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

  @InjectMocks
  private ProfileRequestService profileRequestService;

  @Mock
  private BusinessProfileRequestRepository businessProfileRequestRepository;

  @Mock
  private ProductValidationFactory productValidationFactory;

  @Mock
  private BusinessProfileService businessProfileService;

  @Mock
  private ProfileSubscriptionService profileSubscriptionService;

  @Mock
  private ProfileUpdateRequestProducer profileUpdateRequestProducer;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }


  @Test
  public void testGetProfileRequestByProfileId() {

    String profileId = "123";

    when(businessProfileRequestRepository.findByProfileId(profileId)).thenReturn(new ArrayList<>());

    List<BusinessProfileRequest> requests = profileRequestService.getProfileRequestByProfileId(profileId);

    assertNotNull(requests);
  }

//  @Test
  public void testGetProfileRequest() {

    String requestId = "456";

    when(businessProfileRequestRepository.findByRequestId(requestId)).thenReturn(new BusinessProfileRequestEntity());

    BusinessProfileRequest request = profileRequestService.getProfileRequest(requestId);

    assertNotNull(request);
  }

//  @Test
  public void testUpdateRequestStatus() {
    BusinessProfileRequest request = new BusinessProfileRequest();
    ApprovalStatus status = ApprovalStatus.APPROVED;

    when(businessProfileRequestRepository.saveAndUpdate(any())).thenReturn(new BusinessProfileRequestEntity());

    profileRequestService.updateRequestStatus(request, status);

    verify(businessProfileRequestRepository, times(1)).saveAndUpdate(any());
  }
}
