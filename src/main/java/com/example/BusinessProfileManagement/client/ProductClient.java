package com.example.BusinessProfileManagement.client;

import com.example.BusinessProfileManagement.model.ProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.Product;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;


@Component
public interface ProductClient {
  CompletableFuture<ApprovalStatus> getApproval(Product product, ProfileUpdateRequest profileId);
}
