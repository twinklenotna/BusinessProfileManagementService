package com.example.BusinessProfileManagement.model.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import java.util.UUID;

public class UUIDConverter implements DynamoDBTypeConverter<String, UUID> {
  @Override
  public String convert(UUID uuid) {
    return uuid.toString();
  }

  @Override
  public UUID unconvert(String uuidString) {
    return UUID.fromString(uuidString);
  }
}
