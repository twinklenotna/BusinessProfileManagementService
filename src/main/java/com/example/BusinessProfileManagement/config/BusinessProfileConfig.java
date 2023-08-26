package com.example.BusinessProfileManagement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BusinessProfileConfig {
  @Value("${spring.business.profile.encryptionKey}")
  public static String encryptionKey;
}
