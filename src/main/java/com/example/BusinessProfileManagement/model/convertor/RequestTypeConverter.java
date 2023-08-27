package com.example.BusinessProfileManagement.model.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.example.BusinessProfileManagement.model.enums.RequestType;


public class RequestTypeConverter implements DynamoDBTypeConverter<String, RequestType> {
  @Override
  public String convert(RequestType approvalStatus) {
    return approvalStatus.toString();
  }

  @Override
  public RequestType unconvert(String value) {
    return RequestType.valueOf(value);
  }
}
