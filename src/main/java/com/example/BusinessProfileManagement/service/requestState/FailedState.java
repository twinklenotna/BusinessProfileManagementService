package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import org.springframework.stereotype.Component;


@Component
public class FailedState implements ConsumerState {
  @Override
  public void processRequest(BusinessProfileRequest request,
      BusinessProfileRequestContext businessProfileRequestContext) {
    // Perform actions specific to the FAILED state
    throw new BusinessProfileValidationException("could not validate client");
    // Save or update the request
  }
}
