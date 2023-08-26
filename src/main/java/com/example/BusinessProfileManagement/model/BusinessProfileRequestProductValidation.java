package com.example.BusinessProfileManagement.model;

import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProfileRequestProductValidation {
  private String requestId;
  private String productId;
  private ApprovalStatus status;
  private String comments;
}
