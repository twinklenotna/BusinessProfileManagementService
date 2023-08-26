package com.example.BusinessProfileManagement.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class AddressEntity {
  @DynamoDBAttribute
  private String addressLine1;
  @DynamoDBAttribute
  private String addressLine2;
  @DynamoDBAttribute
  private String city;
  @DynamoDBAttribute
  private String state;
  @DynamoDBAttribute
  private String zip;
  @DynamoDBAttribute
  private String country;
}
