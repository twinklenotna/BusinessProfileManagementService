package com.example.BusinessProfileManagement.model.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;


public class RequestTypeConvertor implements DynamoDBTypeConverter<String, RequestType> {
  @Override
  public String convert(RequestType approvalStatus) {
    return approvalStatus.toString();
  }

  @Override
  public RequestType unconvert(String value) {
    return RequestType.valueOf(value);
  }
}
