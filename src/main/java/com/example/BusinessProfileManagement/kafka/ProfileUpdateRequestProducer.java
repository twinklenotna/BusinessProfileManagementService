package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


@Service
public class ProfileUpdateRequestProducer {
  Logger logger = LoggerFactory.getLogger(ProfileUpdateRequestProducer.class);

  private final KafkaTemplate<String, BusinessProfileUpdateRequest> kafkaTemplate;
  @Value("${kafka.topic}")
  private String topicName;
  @Autowired
  public ProfileUpdateRequestProducer(KafkaTemplate<String, BusinessProfileUpdateRequest> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendProfileUpdateRequestWithKey(String key, BusinessProfileUpdateRequest request) {
    CompletableFuture<SendResult<String, BusinessProfileUpdateRequest>> future = kafkaTemplate.send(topicName, key, request);
    future.thenAccept(result -> {
      logger.info("Request was delivered with following offset: " + result.getRecordMetadata().offset());
    }).exceptionally(ex -> {
      logger.warn("Profile Request could not be delivered. " + ex.getMessage());
      return null;
    });
  }
}

