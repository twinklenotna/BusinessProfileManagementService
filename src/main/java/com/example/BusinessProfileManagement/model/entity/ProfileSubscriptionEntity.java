package com.example.BusinessProfileManagement.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "profile_subscriptions")
public class ProfileSubscriptionEntity {
  @DynamoDBHashKey
  private String profileId;
  @DynamoDBAttribute
  private Set<String> subscriptions;
}
