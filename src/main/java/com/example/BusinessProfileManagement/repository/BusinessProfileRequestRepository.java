package com.example.BusinessProfileManagement.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BusinessProfileRequestRepository {
  final DynamoDBMapper _dynamoDBMapper;

  public BusinessProfileRequestRepository(DynamoDBMapper _dynamoDBMapper) {
    this._dynamoDBMapper = _dynamoDBMapper;
  }

  public BusinessProfileRequestEntity saveAndUpdate(BusinessProfileRequestEntity businessProfileRequestEntity) {
    _dynamoDBMapper.save(businessProfileRequestEntity);
    return businessProfileRequestEntity;
  }

  public List<BusinessProfileRequestEntity> findByProfileId(String profileId) {
    String gsiName = "profileId";
    String gsiPartitionKey = "profileId";

    Map<String, AttributeValue> eav = new HashMap<>();
    eav.put(":val1", new AttributeValue().withS(gsiPartitionKey));

    DynamoDBQueryExpression<BusinessProfileRequestEntity> queryExpression = new DynamoDBQueryExpression<BusinessProfileRequestEntity>()
        .withIndexName(gsiName)
        .withKeyConditionExpression("profileId = :val1")
        .withExpressionAttributeValues(eav);

    List<BusinessProfileRequestEntity> results = _dynamoDBMapper.query(BusinessProfileRequestEntity.class, queryExpression);
    return results;
  }

  public BusinessProfileRequestEntity findByRequestId(String requestId) {
    return _dynamoDBMapper.load(BusinessProfileRequestEntity.class, requestId);
  }
}

