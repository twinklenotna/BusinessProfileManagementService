package com.example.BusinessProfileManagement.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.BusinessProfileManagement.model.entity.ProfileSubscriptionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProfileSubscriptionRepositoryTest {

  @Mock
  private DynamoDBMapper dynamoDBMapper;

  @InjectMocks
  private ProfileSubscriptionRepository repository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSave() {
    ProfileSubscriptionEntity entity = new ProfileSubscriptionEntity();
    entity.setProfileId("testProfileId");

    doNothing().when(dynamoDBMapper).save(entity);

    ProfileSubscriptionEntity savedEntity = repository.save(entity);

    verify(dynamoDBMapper, times(1)).save(entity);

    assertEquals(entity, savedEntity);
  }

  @Test
  public void testGetProfileById() {
    String profileId = "testProfileId";

    ProfileSubscriptionEntity entity = new ProfileSubscriptionEntity();
    entity.setProfileId(profileId);

    when(dynamoDBMapper.load(ProfileSubscriptionEntity.class, profileId)).thenReturn(entity);

    ProfileSubscriptionEntity foundEntity = repository.getProfileById(profileId);
    verify(dynamoDBMapper, times(1)).load(ProfileSubscriptionEntity.class, profileId);

    assertEquals(entity, foundEntity);
  }
}

