package com.example.BusinessProfileManagement.config;

import com.example.BusinessProfileManagement.model.enums.Product;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties
@Component
@ConfigurationProperties(prefix = "product")
public class ProductUrlMapConfig {
  private Map<String, String> url;

  public Map<String, String> getUrl() {
    return url;
  }

  public void setUrl(Map<String, String> url) {
    this.url = url;
  }
}