package com.example.BusinessProfileManagement.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.Serializable;
import lombok.Data;


@Data
public class TaxInfo {
  private String pan;
  private String ein;

  // Convert TaxInfo to JSON
  public String toJson() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(this);
  }

  // Convert JSON to TaxInfo
  public static TaxInfo fromJson(String json) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, TaxInfo.class);
  }
}
