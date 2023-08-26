package com.example.BusinessProfileManagement.model.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.example.BusinessProfileManagement.model.entity.AddressEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AddressConverter implements DynamoDBTypeConverter<String, AddressEntity> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convert(AddressEntity addressEntity) {
    try {
      return objectMapper.writeValueAsString(addressEntity);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting Address to JSON", e);
    }
  }

  @Override
  public AddressEntity unconvert(String addressJson) {
    try {
      return objectMapper.readValue(addressJson, AddressEntity.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting JSON to Address", e);
    }
  }
}

