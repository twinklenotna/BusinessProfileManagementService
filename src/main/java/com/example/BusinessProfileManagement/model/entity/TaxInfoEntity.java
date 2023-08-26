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
public class TaxInfoEntity {
  @DynamoDBAttribute
  private String pan;
  @DynamoDBAttribute
  private String ein;
}
