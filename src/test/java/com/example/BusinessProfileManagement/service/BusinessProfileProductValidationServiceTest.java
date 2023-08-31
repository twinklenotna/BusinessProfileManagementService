package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.helper.ProductValidationHelper;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestProductValidationMapper;
import com.example.BusinessProfileManagement.repository.BusinessProfileRequestProductValidationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BusinessProfileProductValidationServiceTest {

  @Mock
  private BusinessProfileRequestProductValidationRepository businessProfileRequestProductValidationRepository;

  @InjectMocks
  private BusinessProfileProductValidationService _businessProfileProductValidationService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetRequestProductValidations() {
    String requestId = "123";
    List<BusinessProfileRequestProductValidationEntity> entities = new ArrayList<>();
    BusinessProfileRequestProductValidationEntity entity =
        ProductValidationHelper.createProfileRequestProductValidationEntity(requestId, ApprovalStatus.APPROVED);
    entities.add(entity);
    when(businessProfileRequestProductValidationRepository.findByRequestId(requestId)).thenReturn(entities);

    List<BusinessProfileRequestProductValidation> validations = _businessProfileProductValidationService.getRequestProductValidations(requestId);

    assertEquals(1, validations.size());
    assertEquals(entity.getRequestId(), validations.get(0).getRequestId());
  }

  @Test
  public void testSaveBusinessProfileRequestProductValidation() {
    String requestId = "123";
    BusinessProfileRequestProductValidation validation =
        ProductValidationHelper.createProfileRequestProductValidation(requestId, ApprovalStatus.APPROVED);
    BusinessProfileRequestProductValidationEntity entity = BusinessProfileRequestProductValidationMapper.INSTANCE.dtoToEntity(validation);
    when(businessProfileRequestProductValidationRepository.saveAndFlush(entity)).thenReturn(entity);

    BusinessProfileRequestProductValidation savedValidation = _businessProfileProductValidationService.saveBusinessProfileRequestProductValidation(validation);

    assertEquals(validation.getRequestId(), savedValidation.getRequestId());
  }
}

