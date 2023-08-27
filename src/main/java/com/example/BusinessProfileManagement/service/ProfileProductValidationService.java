package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestProductValidationMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestProductValidationRepository;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProfileProductValidationService {
  Logger logger = LoggerFactory.getLogger(ProfileProductValidationService.class);
  final BusinessProfileRequestProductValidationRepository _businessProfileRequestProductValidationRepository;

  public ProfileProductValidationService(
      BusinessProfileRequestProductValidationRepository businessProfileRequestProductValidationRepository) {
    this._businessProfileRequestProductValidationRepository = businessProfileRequestProductValidationRepository;
  }

  public List<BusinessProfileRequestProductValidation> getRequestProductValidations(String requestId) {
    List<BusinessProfileRequestProductValidationEntity> profileRequestProductValidations =
        _businessProfileRequestProductValidationRepository.findByRequestId(requestId);
    List<BusinessProfileRequestProductValidation> validations = new ArrayList<>();
    for(BusinessProfileRequestProductValidationEntity productValidation : profileRequestProductValidations) {
      validations.add(BusinessProfileRequestProductValidationMapper.INSTANCE.entityToDto(productValidation));
    }
    return validations;
  }

  public BusinessProfileRequestProductValidation saveBusinessProfileRequestProductValidation(
      BusinessProfileRequestProductValidation validation) {
    _businessProfileRequestProductValidationRepository.saveAndFlush(BusinessProfileRequestProductValidationMapper.INSTANCE.dtoToEntity(
        validation));
    return validation;
  }
}
