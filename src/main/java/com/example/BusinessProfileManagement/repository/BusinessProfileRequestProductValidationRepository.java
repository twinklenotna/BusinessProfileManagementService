package com.example.BusinessProfileManagement.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public class BusinessProfileRequestProductValidationRepository  {
  DynamoDBMapper dynamoDBMapper;

  BusinessProfileRequestProductValidationRepository(DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  public BusinessProfileRequestProductValidationEntity saveAndFlush(BusinessProfileRequestProductValidationEntity entity) {
    dynamoDBMapper.save(entity);
    return entity;
  }

  public List<BusinessProfileRequestProductValidationEntity> findByRequestId(String requestId) {

    Map<String, AttributeValue> eav = new HashMap<>();
    eav.put(":val1", new AttributeValue().withS(requestId));

    DynamoDBQueryExpression<BusinessProfileRequestProductValidationEntity> queryExpression =
        new DynamoDBQueryExpression<BusinessProfileRequestProductValidationEntity>()
        .withKeyConditionExpression("requestId = :val1")
        .withExpressionAttributeValues(eav);

    List<BusinessProfileRequestProductValidationEntity> results =
        dynamoDBMapper.query(BusinessProfileRequestProductValidationEntity.class, queryExpression);

    return results;
  }
}
