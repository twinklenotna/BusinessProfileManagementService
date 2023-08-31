package com.example.BusinessProfileManagement.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileRequestEntity;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mockito.internal.stubbing.defaultanswers.ForwardsInvocations;


public class BusinessProfileUpdateRequestRepositoryTest {
  private final String PROFILE_ID = "12346";
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

    assertEquals(entity.getRequestId(), savedEntity.getRequestId());
    assertEquals(entity.getProfileId(), savedEntity.getProfileId());
    assertEquals(entity.getBusinessProfile(), savedEntity.getBusinessProfile());
    assertEquals(entity.getStatus(), savedEntity.getStatus());
    assertEquals(entity.getSubscriptions(), savedEntity.getSubscriptions());
    assertEquals(entity.getRequestType(), savedEntity.getRequestType());
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

  @Test
  public void testFindByprofileId() {
    BusinessProfileRequestEntity expectedEntity = ProfileRequestHelper.createBusinessProfileRequestEntity(PROFILE_ID);

    List<BusinessProfileRequestEntity> expectedResult = Arrays.asList(expectedEntity);

    PaginatedQueryList listMock = mock(PaginatedQueryList.class);
    when(listMock.listIterator()).thenReturn(expectedResult.listIterator());
    when(dynamoDBMapper.query(eq(BusinessProfileRequestEntity.class), any(DynamoDBQueryExpression.class)))
        .thenReturn(mock(PaginatedQueryList.class, withSettings().defaultAnswer(new ForwardsInvocations(expectedResult))));


    List<BusinessProfileRequestEntity> results = repository.findByprofileId(PROFILE_ID);

    assertNotNull(results);
    assertEquals(1, results.size());
    assertEquals(expectedEntity, results.get(0));
  }
}

