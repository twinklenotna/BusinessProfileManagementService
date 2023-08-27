package com.example.BusinessProfileManagement.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.example.BusinessProfileManagement.model.convertor.ApprovalStatusConverter;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "business_profile_request_product_validation")
public class BusinessProfileRequestProductValidationEntity {
  @DynamoDBHashKey
  private String requestId;
  @DynamoDBRangeKey(attributeName="productId")
  private String productId;
  @DynamoDBAttribute
  @DynamoDBTypeConverted(converter = ApprovalStatusConverter.class)
  private ApprovalStatus status;
  @DynamoDBAttribute
  private String comments;
}
