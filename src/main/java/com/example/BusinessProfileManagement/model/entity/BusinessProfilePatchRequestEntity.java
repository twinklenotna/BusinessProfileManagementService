package com.example.BusinessProfileManagement.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.example.BusinessProfileManagement.model.TaxInfo;
import com.example.BusinessProfileManagement.model.convertor.ProfileStatusConvertor;
import com.example.BusinessProfileManagement.model.convertor.TaxInfoEncryptConverter;
import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBDocument
public class BusinessProfilePatchRequestEntity {
  @DynamoDBAttribute
  private String profileId;
  @DynamoDBAttribute
  private String companyName;
  @DynamoDBAttribute
  private String legalName;
  @DynamoDBAttribute
  private AddressEntity businessAddress;
  @DynamoDBAttribute
  private AddressEntity legalAddress;
  @DynamoDBTypeConverted(converter = TaxInfoEncryptConverter.class)
  private TaxInfo taxInfo;
  @DynamoDBAttribute
  private String email;
  @DynamoDBAttribute
  private String website;
  @DynamoDBAttribute
  private Long updateTime;
  @DynamoDBAttribute
  private Long creationTime;
  @DynamoDBAttribute
  @DynamoDBTypeConverted(converter = ProfileStatusConvertor.class)
  private ProfileStatus status;
}
