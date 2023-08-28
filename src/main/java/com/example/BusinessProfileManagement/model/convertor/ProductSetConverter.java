package com.example.BusinessProfileManagement.model.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;


public class ProductSetConverter implements DynamoDBTypeConverter<String, Set<String>> {

  public ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convert(Set<String> products) {
    try {
      return objectMapper.writeValueAsString(products);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting Set<Product> to JSON", e);
    }
  }

  @Override
  public Set<String> unconvert(String productsJson) {
    try {
      return objectMapper.readValue(productsJson, new TypeReference<Set<String>>() {});
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Error converting JSON to Set<Product>", e);
    }
  }
}

