package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import org.springframework.stereotype.Component;


@Component
public class FailedState implements ConsumerState {
  @Override
  public void processRequest(BusinessProfileUpdateRequest request,
                             BusinessProfileRequestContext businessProfileRequestContext) {
    throw new BusinessProfileValidationException("could not validate client");
  }
}
