package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import java.util.concurrent.CompletableFuture;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileUpdateRequestProducer {

  private final KafkaTemplate<String, BusinessProfileUpdateRequest> kafkaTemplate;
  @Value("${kafka.topic}")
  public String topicName;


  /**
   * To send the profile update request to kafka
   * @param request profile update request
   */
  public void sendProfileUpdateRequestWithKey(String key, BusinessProfileUpdateRequest request) {
    CompletableFuture<SendResult<String, BusinessProfileUpdateRequest>> future = kafkaTemplate.send(topicName, key, request);
    future.thenAccept(result -> {
      log.info("Request was delivered with following offset: " + result.getRecordMetadata().offset());
    }).exceptionally(ex -> {
      log.warn("Profile Request could not be delivered. " + ex.getMessage());
      return null;
    });
  }
}

