package com.example.BusinessProfileManagement.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import org.springframework.stereotype.Repository;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;


@Repository
public class BusinessProfileRepository {
  DynamoDBMapper dynamoDBMapper;

  BusinessProfileRepository(DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  public BusinessProfileEntity save(BusinessProfileEntity profile) {
    dynamoDBMapper.save(profile);
    return profile;
  }

  public BusinessProfileEntity getProfileById(String profileId) {
    return dynamoDBMapper.load(BusinessProfileEntity.class, profileId);
  }

  public void delete(String profileId) {
    BusinessProfileEntity emp = dynamoDBMapper.load(BusinessProfileEntity.class, profileId);
    dynamoDBMapper.delete(emp);
  }
}
