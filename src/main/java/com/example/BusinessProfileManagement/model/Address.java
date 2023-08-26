package com.example.BusinessProfileManagement.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.Data;


@Data
public class Address {
  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String zip;
  private String country;
}
