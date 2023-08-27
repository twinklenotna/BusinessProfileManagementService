package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ProfileUpdateRequestProducerTest {

  @Mock
  private KafkaTemplate<String, BusinessProfileRequest> kafkaTemplate;

  @InjectMocks
  private ProfileUpdateRequestProducer profileUpdateRequestProducer;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSendProfileUpdateRequestWithKey() throws ExecutionException, InterruptedException {
    BusinessProfileRequest request = new BusinessProfileRequest();
    request.setRequestId("testRequestId");
    request.setProfileId("testProfileId");
    String key = "testKey";

    SendResult<String, BusinessProfileRequest> sendResult = new SendResult<>(null, null);
    CompletableFuture<SendResult<String, BusinessProfileRequest>> future = CompletableFuture.completedFuture(sendResult);

    when(kafkaTemplate.send(any(), any(), any())).thenReturn(future);

    profileUpdateRequestProducer.sendProfileUpdateRequestWithKey(key, request);

    Mockito.verify(kafkaTemplate, Mockito.times(1)).send("profileRequest", key, request);
  }
}