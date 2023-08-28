package com.example.BusinessProfileManagement.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BusinessProfileRepositoryTest {
  private final String PROFILE_ID = "12346";
  @Mock
  private DynamoDBMapper dynamoDBMapper;

  @InjectMocks
  private BusinessProfileRepository businessProfileRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSave() {
    BusinessProfileEntity profile = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);

    doNothing().when(dynamoDBMapper).save(profile);

    BusinessProfileEntity savedProfile = businessProfileRepository.save(profile);

    verify(dynamoDBMapper, times(1)).save(profile);

    assertEquals(profile.getProfileId(), savedProfile.getProfileId());
    assertEquals(profile.getStatus(), savedProfile.getStatus());
    assertEquals(profile.getEmail(), savedProfile.getEmail());
    assertEquals(profile.getBusinessAddress(), savedProfile.getBusinessAddress());
    assertEquals(profile.getWebsite(), savedProfile.getWebsite());
    assertEquals(profile.getLegalAddress(), savedProfile.getLegalAddress());
    assertEquals(profile.getCompanyName(), savedProfile.getCompanyName());
    assertEquals(profile.getLegalName(), savedProfile.getLegalName());
    assertEquals(profile.getTaxInfoEntity(), savedProfile.getTaxInfoEntity());
    assertEquals(profile, savedProfile);
  }

  @Test
  public void testGetProfileById() {
    BusinessProfileEntity profile = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);

    when(dynamoDBMapper.load(BusinessProfileEntity.class, PROFILE_ID)).thenReturn(profile);

    BusinessProfileEntity retrievedProfile = businessProfileRepository.getProfileById(PROFILE_ID);

    verify(dynamoDBMapper, times(1)).load(BusinessProfileEntity.class, PROFILE_ID);

    assertEquals(profile, retrievedProfile);
  }

  @Test
  public void testDelete() {
    BusinessProfileEntity expectedProfile = ProfileHelper.createBusinessProfileEntity(PROFILE_ID);

    when(dynamoDBMapper.load(BusinessProfileEntity.class, PROFILE_ID)).thenReturn(expectedProfile);

    businessProfileRepository.delete(PROFILE_ID);

    verify(dynamoDBMapper, times(1)).load(BusinessProfileEntity.class, PROFILE_ID);
    verify(dynamoDBMapper, times(1)).delete(expectedProfile);
  }
}

