package com.example.BusinessProfileManagement.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BusinessProfileRepositoryTest {

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
    BusinessProfileEntity profile = new BusinessProfileEntity();
    profile.setProfileId("testProfileId");

    doNothing().when(dynamoDBMapper).save(profile);

    BusinessProfileEntity savedProfile = businessProfileRepository.save(profile);

    verify(dynamoDBMapper, times(1)).save(profile);

    assertEquals(profile, savedProfile);
  }

  @Test
  public void testGetProfileById() {
    String profileId = "testProfileId";
    BusinessProfileEntity expectedProfile = new BusinessProfileEntity();
    expectedProfile.setProfileId(profileId);

    when(dynamoDBMapper.load(BusinessProfileEntity.class, profileId)).thenReturn(expectedProfile);

    BusinessProfileEntity retrievedProfile = businessProfileRepository.getProfileById(profileId);

    verify(dynamoDBMapper, times(1)).load(BusinessProfileEntity.class, profileId);

    assertEquals(expectedProfile, retrievedProfile);
  }

  @Test
  public void testDelete() {
    String profileId = "testProfileId";
    BusinessProfileEntity expectedProfile = new BusinessProfileEntity();
    expectedProfile.setProfileId(profileId);

    when(dynamoDBMapper.load(BusinessProfileEntity.class, profileId)).thenReturn(expectedProfile);

    businessProfileRepository.delete(profileId);

    verify(dynamoDBMapper, times(1)).load(BusinessProfileEntity.class, profileId);
    verify(dynamoDBMapper, times(1)).delete(expectedProfile);
  }
}

