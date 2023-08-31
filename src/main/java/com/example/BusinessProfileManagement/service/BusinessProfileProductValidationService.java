package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestProductValidationMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestProductValidationRepository;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessProfileProductValidationService {
  private final BusinessProfileRequestProductValidationRepository businessProfileRequestProductValidationRepository;

  public List<BusinessProfileRequestProductValidation> getRequestProductValidations(String requestId) {
    List<BusinessProfileRequestProductValidationEntity> profileRequestProductValidations =
        businessProfileRequestProductValidationRepository.findByRequestId(requestId);
    List<BusinessProfileRequestProductValidation> validations = new ArrayList<>();
    for(BusinessProfileRequestProductValidationEntity productValidation : profileRequestProductValidations) {
      validations.add(BusinessProfileRequestProductValidationMapper.INSTANCE.entityToDto(productValidation));
    }
    return validations;
  }

  public BusinessProfileRequestProductValidation saveBusinessProfileRequestProductValidation(
      BusinessProfileRequestProductValidation validation) {
    businessProfileRequestProductValidationRepository.saveAndFlush(BusinessProfileRequestProductValidationMapper.INSTANCE.dtoToEntity(
        validation));
    return validation;
  }
}
