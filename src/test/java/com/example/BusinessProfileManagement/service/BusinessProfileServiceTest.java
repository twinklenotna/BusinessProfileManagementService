package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.kafka.ProfileUpdateRequestProducer;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRepository;

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
  private BusinessProfileRequestService _businessProfileRequestService;

  @Mock
  private ProfileUpdateRequestProducer profileUpdateRequestProducer;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testUpdateProfile() {
    BusinessProfilePatchRequest profile = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);

    BusinessProfileUpdateRequest businessProfileUpdateRequest =
        ProfileRequestHelper.createBusinessProfileRequest(profile, RequestType.UPDATE);

    when(_businessProfileRequestService.createBusinessProfileRequest(profile, RequestType.UPDATE, new HashSet<>()))
        .thenReturn(businessProfileUpdateRequest);

    businessProfileService.updateProfile(profile);

    verify(_businessProfileRequestService, times(1)).createBusinessProfileRequest(any(), any(), any());
    verify(profileUpdateRequestProducer, times(1)).sendProfileUpdateRequestWithKey(any(), any());
  }

  @Test
  public void testUpdateProfileEntity() {
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileEntity profileEntity = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);

    when(profileRepository.save(any())).thenReturn(profileEntity);
    when(businessProfileMapper.toEntity(profile)).thenReturn(profileEntity);

    businessProfileService.updateBusinessProfileEntity(profile);

    verify(profileRepository, times(1)).save(any());
  }

  @Test
  public void testUpdateProfileWithSubscriptions() {
    BusinessProfilePatchRequest profile = ProfileHelper.createBusinessProfilePatchRequest(PROFILE_ID);
    BusinessProfileUpdateRequest businessProfileUpdateRequest =
        ProfileRequestHelper.createBusinessProfileRequest(profile, RequestType.SUBSCRIBE);

    when(_businessProfileRequestService.createBusinessProfileRequest(eq(profile), eq(RequestType.SUBSCRIBE), any()))
        .thenReturn(businessProfileUpdateRequest);

    businessProfileService.updateProfile(profile, businessProfileUpdateRequest.getSubscriptions());

    verify(_businessProfileRequestService, times(1)).createBusinessProfileRequest(eq(profile), eq(RequestType.SUBSCRIBE), any());
    verify(profileUpdateRequestProducer, times(1)).sendProfileUpdateRequestWithKey(any(), any());
  }

  @Test
  public void testCreateProfileRequest() {
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileEntity profileEntity = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);

    when(profileRepository.save(any())).thenReturn(profileEntity);
    when(businessProfileMapper.toEntity(profile)).thenReturn(profileEntity);
    when(businessProfileMapper.toDto(profileEntity)).thenReturn(profile);

    BusinessProfile profileId = businessProfileService.createProfileRequest(profile);

    assertNotNull(profileId.getProfileId());
  }

  @Test
  public void testDeleteProfile() {
    doNothing().when(profileRepository).delete(PROFILE_ID);

    businessProfileService.deleteProfile(PROFILE_ID);

    verify(profileRepository, times(1)).delete(PROFILE_ID);
  }

  @Test
  public void testDeleteProfileException() {
    doThrow(new RuntimeException("profile not found")).when(profileRepository).delete(PROFILE_ID);

    assertThrows(BusinessProfileNotFoundException.class, () -> {
      businessProfileService.deleteProfile(PROFILE_ID);
    });
  }

  @Test
  public void testGetProfileById() {
    BusinessProfile profile = ProfileHelper.createBusinessProfile(PROFILE_ID);
    BusinessProfileEntity profileEntity = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);

    when(profileRepository.getProfileById(PROFILE_ID)).thenReturn(profileEntity);
    when(businessProfileMapper.toEntity(profile)).thenReturn(profileEntity);
    when(businessProfileMapper.toDto(profileEntity)).thenReturn(profile);

    BusinessProfile profileResponse = businessProfileService.getProfileById(PROFILE_ID);

    verify(profileRepository, times(1)).getProfileById(PROFILE_ID);

    assertNotNull(profileResponse);
    assertEquals(profileResponse.getProfileId(), profile.getProfileId());
  }

  @Test
  public void testGetProfileByIdNotFound() {
    when(profileRepository.getProfileById(PROFILE_ID)).thenReturn(null);
    assertThrows(BusinessProfileNotFoundException.class, () -> {
      businessProfileService.getProfileById(PROFILE_ID);
    });
  }
}

