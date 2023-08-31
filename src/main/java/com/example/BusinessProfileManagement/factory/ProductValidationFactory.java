package com.example.BusinessProfileManagement.factory;

import com.example.BusinessProfileManagement.client.DefaultProductClient;
import com.example.BusinessProfileManagement.client.ProductClient;
import com.example.BusinessProfileManagement.client.QBOProductClient;
import com.example.BusinessProfileManagement.config.ProductUrlMapConfig;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestProductValidation;
import org.springframework.stereotype.Service;


@Service
public class ProductValidationFactory {
  public static final String QUICKBOOKS = "QUICKBOOKS";
  private final DefaultProductClient defaultProductClient;
  private final QBOProductClient qboProductClient;
  private final ProductUrlMapConfig productUrlMapConfig;

  public ProductValidationFactory(DefaultProductClient defaultProductClient, QBOProductClient qboProductClient, ProductUrlMapConfig productUrlMapConfig) {
    this.defaultProductClient = defaultProductClient;
    this.qboProductClient = qboProductClient;
    this.productUrlMapConfig = productUrlMapConfig;
  }

  public ProductClient getClient(String productId) {
    if(productId.equals(QUICKBOOKS)) {
      return qboProductClient;
    } else if (productUrlMapConfig.getUrl().containsKey(productId)) {
      return defaultProductClient;
    } else {
      throw new IllegalArgumentException("Unknown product: " + productId);
    }
  }
}

