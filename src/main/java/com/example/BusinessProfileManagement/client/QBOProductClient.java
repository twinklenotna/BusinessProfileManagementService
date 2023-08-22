package com.example.BusinessProfileManagement.client;

import com.example.BusinessProfileManagement.model.ProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.Product;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;


@Component
public class QBOProductClient implements ProductClient{
  @Override
  public CompletableFuture<ApprovalStatus> getApproval(Product product, ProfileUpdateRequest profileId) {
    return CompletableFuture.completedFuture(ApprovalStatus.APPROVED);
  }
}
