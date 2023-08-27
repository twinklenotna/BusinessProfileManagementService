package com.example.BusinessProfileManagement.model.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.example.BusinessProfileManagement.model.enums.ProfileStatus;


public class ProfileStatusConverter implements DynamoDBTypeConverter<String, ProfileStatus> {
  @Override
  public String convert(ProfileStatus approvalStatus) {
    return approvalStatus.toString();
  }

  @Override
  public ProfileStatus unconvert(String value) {
    return ProfileStatus.valueOf(value);
  }
}
