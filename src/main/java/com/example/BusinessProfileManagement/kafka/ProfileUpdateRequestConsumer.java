package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.service.requestState.BusinessProfileRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProfileUpdateRequestConsumer {
  Logger logger = LoggerFactory.getLogger(ProfileUpdateRequestConsumer.class);
  final BusinessProfileRequestContext _businessProfileRequestContext;

  public ProfileUpdateRequestConsumer(BusinessProfileRequestContext _businessProfileRequestContext) {
    this._businessProfileRequestContext = _businessProfileRequestContext;
  }

  @KafkaListener(topics = "profileRequest", groupId = "profileRequestValidationGroup")
  public void consumeProfileUpdateRequest(BusinessProfileUpdateRequest request) {
    logger.info("Processing request with requestId: {}  and profileId: {} ", request.getRequestId(), request.getProfileId());
    _businessProfileRequestContext.processRequest(request);
  }
}

