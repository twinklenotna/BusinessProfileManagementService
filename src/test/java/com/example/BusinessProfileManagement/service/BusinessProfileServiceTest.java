package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.kafka.ProfileUpdateRequestProducer;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileMapper;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRepository;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestRepository;
import java.util.HashSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BusinessProfileServiceTest {
  private final String PROFILE_ID = "12346";
  @InjectMocks
  private BusinessProfileService businessProfileService;

  @Mock BusinessProfileMapper businessProfileMapper;

  @Mock
  private BusinessProfileRepository profileRepository;

  @Mock
  private BusinessProfileRequestRepository businessProfileRequestRepository;

  @Mock
  private ProfileRequestService profileRequestService;

  @Mock
  private ProfileUpdateRequestProducer profileUpdateRequestProducer;

  @Mock
  private BusinessProfileRequestMapper businessProfileRequestMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testUpdateProfile() {
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);

    BusinessProfileRequest businessProfileRequest =
        ProfileRequestHelper.createBusinessProfileRequest(profile, RequestType.UPDATE);

    when(profileRequestService.createBusinessProfileRequestEntity(profile, RequestType.UPDATE, new HashSet<>()))
        .thenReturn(businessProfileRequestMapper.dtoToEntity(businessProfileRequest));

    businessProfileService.updateProfile(profile);

    verify(profileRequestService, times(1)).createBusinessProfileRequestEntity(any(), any(), any());
    verify(profileUpdateRequestProducer, times(1)).sendProfileUpdateRequestWithKey(any(), any());
  }

//  @Test
  public void testCreateProfileRequest() {
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileRequest businessProfileRequest =
        ProfileRequestHelper.createBusinessProfileRequest(profile, RequestType.CREATE);
    BusinessProfileEntity businessProfileEntity = businessProfileMapper.dtoToEntity(profile);

    when(profileRepository.save(any())).thenReturn(businessProfileEntity);
    when(profileRequestService.createBusinessProfileRequestEntity(profile, RequestType.CREATE, new HashSet<>()))
        .thenReturn(businessProfileRequestMapper.dtoToEntity(businessProfileRequest));

    String profileId = businessProfileService.createProfileRequest(profile);

    assertNotNull(profileId);
  }

//  @Test
  public void testDeleteProfile() {
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);

    doNothing().when(profileRepository).delete(PROFILE_ID);

    businessProfileService.deleteProfile(PROFILE_ID);

    verify(profileRepository, times(1)).delete(PROFILE_ID);
  }

//  @Test
  public void testGetProfileById() {
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileEntity businessProfileEntity = businessProfileMapper.dtoToEntity(profile);

    when(profileRepository.getProfileById(PROFILE_ID)).thenReturn(businessProfileEntity);

    BusinessProfile profileResponse = businessProfileService.getProfileById(PROFILE_ID);

    verify(profileRepository, times(1)).getProfileById(PROFILE_ID);

    assertNotNull(profileResponse);
    assertEquals(profileResponse, profile);
  }
}

