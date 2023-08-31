package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.service.requestState.BusinessProfileRequestContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileUpdateRequestConsumer {
  Logger logger = LoggerFactory.getLogger(ProfileUpdateRequestConsumer.class);
  final BusinessProfileRequestContext businessProfileRequestContext;

  @KafkaListener(topics = "${kafka.topic}", groupId = "profileRequestValidationGroup")
  public void consumeProfileUpdateRequest(BusinessProfileUpdateRequest request) {
    logger.info("Processing request with requestId: {}  and profileId: {} ", request.getRequestId(), request.getProfileId());
    businessProfileRequestContext.processRequest(request);
  }
}

