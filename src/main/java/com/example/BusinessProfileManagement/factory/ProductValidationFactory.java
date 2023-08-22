package com.example.BusinessProfileManagement.factory;

import com.example.BusinessProfileManagement.client.DefaultProductClient;
import com.example.BusinessProfileManagement.client.QBOProductClient;
import com.example.BusinessProfileManagement.model.ProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.Product;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProductValidationFactory {
  Map<Product,String> ProductUrlMap = new HashMap<>();

  private final DefaultProductClient defaultProductClient;
  private final QBOProductClient qboProductClient;

  public ProductValidationFactory(DefaultProductClient defaultProductClient, QBOProductClient qboProductClient) {
    this.defaultProductClient = defaultProductClient;
    this.qboProductClient = qboProductClient;
  }

  public CompletableFuture<ApprovalStatus> createValidationTask(Product product, ProfileUpdateRequest request) {
    switch (product) {
      case QUICKBOOKS:
        return qboProductClient.getApproval(product, request);
      case PAYMENTS:
      case PAYROLL:
      case TSHEETS:
        return defaultProductClient.getApproval(product, request);
      default:
        throw new IllegalArgumentException("Unknown product: " + product);
    }
  }
}

