package com.example.BusinessProfileManagement.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessProfileUpdateRequestRepositoryTest {

  @Mock
  private DynamoDBMapper dynamoDBMapper;

  @InjectMocks
  private BusinessProfileRequestRepository repository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSaveAndUpdate() {
    BusinessProfileRequestEntity entity = new BusinessProfileRequestEntity();
    entity.setRequestId("testRequestId");

    doNothing().when(dynamoDBMapper).save(entity);

    BusinessProfileRequestEntity savedEntity = repository.saveAndUpdate(entity);

    verify(dynamoDBMapper, times(1)).save(entity);

    assertEquals(entity, savedEntity);
  }


  @Test
  public void testFindByRequestId() {
    String requestId = "testRequestId";

    BusinessProfileRequestEntity entity = new BusinessProfileRequestEntity();
    entity.setRequestId(requestId);

    when(dynamoDBMapper.load(BusinessProfileRequestEntity.class, requestId)).thenReturn(entity);

    BusinessProfileRequestEntity foundEntity = repository.findByRequestId(requestId);

    verify(dynamoDBMapper, times(1)).load(BusinessProfileRequestEntity.class, requestId);

    assertEquals(entity, foundEntity);
  }
}

