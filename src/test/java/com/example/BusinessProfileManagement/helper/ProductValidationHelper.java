package com.example.BusinessProfileManagement.helper;

import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ProductValidationHelper {
  public static BusinessProfileRequestProductValidation createProfileRequestProductValidation(String requestId, ApprovalStatus status) {
    BusinessProfileRequestProductValidation businessProfileRequestProductValidation =
        new BusinessProfileRequestProductValidation();
    businessProfileRequestProductValidation.setRequestId(requestId);
    businessProfileRequestProductValidation.setStatus(status);
    businessProfileRequestProductValidation.setProductId(UUID.randomUUID().toString());
    return businessProfileRequestProductValidation;
  }

  public static BusinessProfileRequestProductValidationEntity createProfileRequestProductValidationEntity(String requestId, ApprovalStatus status) {
    BusinessProfileRequestProductValidationEntity businessProfileRequestProductValidation =
        new BusinessProfileRequestProductValidationEntity();
    businessProfileRequestProductValidation.setRequestId(requestId);
    businessProfileRequestProductValidation.setStatus(status);
    businessProfileRequestProductValidation.setProductId(UUID.randomUUID().toString());
    return businessProfileRequestProductValidation;
  }

  public static List<BusinessProfileRequestProductValidation> createProfileRequestProductValidations(int validations,
      String requestId, ApprovalStatus status) {
    List<BusinessProfileRequestProductValidation> businessProfileRequestProductValidations = new ArrayList<>();
    for(int i=0; i < validations; i++) {
      businessProfileRequestProductValidations.add(
          createProfileRequestProductValidation(requestId, status));
    }
    return businessProfileRequestProductValidations;
  }

  public static List<BusinessProfileRequestProductValidationEntity> createProfileRequestProductValidationsEntity(int validations,
      String requestId, ApprovalStatus status) {
    List<BusinessProfileRequestProductValidationEntity> businessProfileRequestProductValidations = new ArrayList<>();
    for(int i=0; i < validations; i++) {
      businessProfileRequestProductValidations.add(
          createProfileRequestProductValidationEntity(requestId, status));
    }
    return businessProfileRequestProductValidations;
  }
}
