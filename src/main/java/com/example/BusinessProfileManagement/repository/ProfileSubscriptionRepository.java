package com.example.BusinessProfileManagement.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.example.BusinessProfileManagement.model.entity.ProfileSubscriptionEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;


@Repository
public class ProfileSubscriptionRepository {
  DynamoDBMapper dynamoDBMapper;
  ProfileSubscriptionRepository(DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }
  public ProfileSubscriptionEntity save(ProfileSubscriptionEntity profileSubs) {
    dynamoDBMapper.save(profileSubs);
    return profileSubs;
  }

  public ProfileSubscriptionEntity getProfileById(String profileId) {
    return dynamoDBMapper.load(ProfileSubscriptionEntity.class, profileId);
  }
}
