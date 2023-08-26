package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.factory.ProductValidationFactory;
import com.example.BusinessProfileManagement.kafka.ProfileUpdateRequestProducer;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestProductValidationRepository;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileValidationServiceTest {

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
  private ProfileRequestService profileRequestService;

  @Mock
  private ProfileUpdateRequestProducer profileUpdateRequestProducer;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

//  @Test
//  public void testApproveRequest_AllProductsApproved() {
//    BusinessProfileRequest request = new BusinessProfileRequest();
//    when(businessProfileService.getProfileById(any())).thenReturn(new BusinessProfile());
//    when(profileSubscriptionService.getSubscriptions(any())).thenReturn(new HashSet<>(
//        Arrays.asList("product1", "product2")));
//    when(productValidationFactory.validateProfile(any(), any())).thenReturn(new BusinessProfileRequestProductValidation());
//    when(businessProfileRequestProductValidationRepository.saveAndFlush(any())).thenReturn(new BusinessProfileRequestProductValidationEntity());
//    when(profileRequestService.updateBusinessProfileRequestEntity(any())).thenReturn(new BusinessProfileRequestEntity());
//
//    profileValidationService.approveRequest(request);
//
//    assertEquals(ApprovalStatus.APPROVED, request.getStatus());
//
//  }
//
//  @Test
//  public void testApproveRequest_SomeProductsRejected() {
//    // Similar to the previous test, but you can configure some product validations to return REJECTED status.
//  }
//
//  @Test
//  public void testApproveRequest_ValidationFails() {
//
//    BusinessProfileRequest request = new BusinessProfileRequest();
//
//    when(businessProfileService.getProfileById(any())).thenThrow(new RuntimeException("Validation error"));
//    assertThrows(BusinessProfileValidationException.class, () -> {
//      profileValidationService.approveRequest(request);
//    });
//  }

}

