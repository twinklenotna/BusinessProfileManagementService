package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


@Service
public class ProfileUpdateRequestProducer {
  Logger logger = LoggerFactory.getLogger(ProfileUpdateRequestProducer.class);

  private final KafkaTemplate<String, BusinessProfileRequest> kafkaTemplate;
  @Autowired
  public ProfileUpdateRequestProducer(KafkaTemplate<String, BusinessProfileRequest> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendProfileUpdateRequestWithKey(String key, BusinessProfileRequest request) {
    CompletableFuture<SendResult<String, BusinessProfileRequest>> future = kafkaTemplate.send("profileRequest", key, request);
    future.thenAccept(result -> {
      logger.info("Request was delivered with following offset: " + result.getRecordMetadata().offset());
    }).exceptionally(ex -> {
      logger.warn("Profile Request could not be delivered. " + ex.getMessage());
      return null;
    });
  }
}

