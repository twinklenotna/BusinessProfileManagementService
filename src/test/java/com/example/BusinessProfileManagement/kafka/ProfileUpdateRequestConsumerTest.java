package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.service.requestState.BusinessProfileRequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;


public class ProfileUpdateRequestConsumerTest {
  @InjectMocks
  private ProfileUpdateRequestConsumer profileUpdateRequestConsumer;

  @Mock
  BusinessProfileRequestContext _businessProfileRequestContext;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testConsumeProfileUpdateRequest() {
    BusinessProfileUpdateRequest request = ProfileRequestHelper.createBusinessProfileRequest(
        ProfileHelper.createBusinessProfile("1234"), RequestType.CREATE
    );
    request.setRequestId("testRequestId");
    request.setProfileId("testProfileId");

    profileUpdateRequestConsumer.consumeProfileUpdateRequest(request);
    doNothing().when(_businessProfileRequestContext).processRequest(request);

    verify(_businessProfileRequestContext, Mockito.times(1)).processRequest(request);
  }
}
