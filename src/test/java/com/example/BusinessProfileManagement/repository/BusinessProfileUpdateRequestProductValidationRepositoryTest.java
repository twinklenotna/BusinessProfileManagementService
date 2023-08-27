package com.example.BusinessProfileManagement.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.BusinessProfileManagement.helper.ProductValidationHelper;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestProductValidationEntity;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessProfileUpdateRequestProductValidationRepositoryTest {

  @Mock
  private DynamoDBMapper dynamoDBMapper;

  @InjectMocks
  private BusinessProfileRequestProductValidationRepository repository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSaveAndFlush() {
    BusinessProfileRequestProductValidationEntity entity = new BusinessProfileRequestProductValidationEntity();
    entity.setRequestId("testRequestId");

    doNothing().when(dynamoDBMapper).save(entity);

    BusinessProfileRequestProductValidationEntity savedEntity = repository.saveAndFlush(entity);

    verify(dynamoDBMapper, times(1)).save(entity);

    assertEquals(entity, savedEntity);
  }

  @Test
  public void testFindByRequestId() {
    String requestId = "testRequestId";

    PaginatedQueryList<BusinessProfileRequestProductValidationEntity> queryResult =
        mock(PaginatedQueryList.class);

    BusinessProfileRequestProductValidationEntity entity =
        ProductValidationHelper.createProfileRequestProductValidationEntity(requestId, ApprovalStatus.APPROVED);
    entity.setRequestId(requestId);

    when(queryResult.iterator()).thenReturn(Collections.singletonList(entity).iterator());

    when(dynamoDBMapper.query(
        eq(BusinessProfileRequestProductValidationEntity.class),
        any(DynamoDBQueryExpression.class)
    )).thenReturn(queryResult);

    List<BusinessProfileRequestProductValidationEntity> results = repository.findByRequestId(requestId);

    verify(dynamoDBMapper, times(1)).query(
        eq(BusinessProfileRequestProductValidationEntity.class),
        any(DynamoDBQueryExpression.class)
    );

    assertFalse(results.isEmpty());
  }
}

