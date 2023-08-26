package com.example.BusinessProfileManagement.kafka;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProfileUpdateRequestProducer {

  private final KafkaTemplate<String, BusinessProfileRequest> kafkaTemplate;
  @Autowired
  public ProfileUpdateRequestProducer(KafkaTemplate<String, BusinessProfileRequest> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendProfileUpdateRequestWithKey(String key, BusinessProfileRequest request) {
    kafkaTemplate.send("profileRequest", key, request);
  }
}

