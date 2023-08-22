package com.example.BusinessProfileManagement.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import org.springframework.stereotype.Repository;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;


@Repository
public class BusinessProfileRepository {
  final String PROFILE_ID = "userId";
  DynamoDBMapper dynamoDBMapper;

  BusinessProfileRepository(DynamoDBMapper dynamoDBMapper) {
    this.dynamoDBMapper = dynamoDBMapper;
  }

  public BusinessProfile save(BusinessProfile profile) {
    dynamoDBMapper.save(profile);
    return profile;
  }

  public BusinessProfile getProfileById(String profileId) {
    return dynamoDBMapper.load(BusinessProfile.class, profileId);
  }

  public String delete(String profileId) {
    BusinessProfile emp = dynamoDBMapper.load(BusinessProfile.class, profileId);
    dynamoDBMapper.delete(emp);
    return "Employee Deleted!";
  }

  public String update(String profileId, BusinessProfile employee) {
    dynamoDBMapper.save(employee,
        new DynamoDBSaveExpression()
            .withExpectedEntry("userId",
                new ExpectedAttributeValue(
                    new AttributeValue().withS(profileId)
                )));
    return profileId;
  }

  public BusinessProfile findByProfileIdAndStatus(String profileId, ApprovalStatus approvalStatus) {
    return dynamoDBMapper.load(BusinessProfile.class, profileId);
  }
}
