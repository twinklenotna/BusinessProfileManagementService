package com.example.BusinessProfileManagement.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class BusinessProfileRequestRepository {
  final DynamoDBMapper dynamoDBMapper;

  public BusinessProfileRequestRepository(DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  public BusinessProfileRequestEntity saveAndUpdate(BusinessProfileRequestEntity businessProfileRequestEntity) {
    dynamoDBMapper.save(businessProfileRequestEntity);
    return businessProfileRequestEntity;
  }

  public List<BusinessProfileRequestEntity> findByProfileId(String profileId) {
    String gsiName = "profileId-index";

    BusinessProfileRequestEntity businessProfileRequestEntity = new BusinessProfileRequestEntity();
    businessProfileRequestEntity.setProfileId(profileId);

    DynamoDBQueryExpression<BusinessProfileRequestEntity> queryExpression =
        new DynamoDBQueryExpression<BusinessProfileRequestEntity>()
            .withHashKeyValues(businessProfileRequestEntity)
            .withIndexName(gsiName)
            .withConsistentRead(false);
    List<BusinessProfileRequestEntity> results = dynamoDBMapper.query(BusinessProfileRequestEntity.class, queryExpression);
    return results;
  }

  public BusinessProfileRequestEntity findByRequestId(String requestId) {
    return dynamoDBMapper.load(BusinessProfileRequestEntity.class, requestId);
  }
}

