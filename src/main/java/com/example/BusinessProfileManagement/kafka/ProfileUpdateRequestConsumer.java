package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.service.requestState.BusinessProfileRequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileUpdateRequestConsumer {
  final BusinessProfileRequestContext businessProfileRequestContext;

  /**
   * To consume the profile update request from kafka
   * @param request profile update request
   */
  @KafkaListener(topics = "${kafka.topic}", groupId = "profileRequestValidationGroup")
  public void consumeProfileUpdateRequest(BusinessProfileUpdateRequest request) {
    log.info("Processing request with requestId: {}  and profileId: {} ", request.getRequestId(), request.getProfileId());
    businessProfileRequestContext.processRequest(request);
  }
}

