package com.example.BusinessProfileManagement.model.convertor;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;


public class ApprovalStatusConverter implements DynamoDBTypeConverter<String, ApprovalStatus> {

  @Override
  public String convert(ApprovalStatus approvalStatus) {
    return approvalStatus.toString();
  }

  @Override
  public ApprovalStatus unconvert(String value) {
    return ApprovalStatus.valueOf(value);
  }
}

