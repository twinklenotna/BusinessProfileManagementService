package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.service.ProfileRequestService;
import com.example.BusinessProfileManagement.service.ProfileValidationService;
import com.example.BusinessProfileManagement.service.requestState.BusinessProfileRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProfileUpdateRequestConsumer {
  Logger logger = LoggerFactory.getLogger(ProfileUpdateRequestConsumer.class);
  final BusinessProfileRequestContext _businessProfileRequestContext;

  public ProfileUpdateRequestConsumer(ProfileValidationService profileValidationService,
      BusinessProfileRequestContext _businessProfileRequestContext) {
    this._businessProfileRequestContext = _businessProfileRequestContext;
  }

  @KafkaListener(topics = "profileRequest", groupId = "profileRequestValidationGroup")
  public void consumeProfileUpdateRequest(BusinessProfileRequest request) {
    logger.debug("Processing request with requestId: {}  and profileId: {} ",request.getRequestId(),request.getProfileId());
    _businessProfileRequestContext.processRequest(request);
  }
}

