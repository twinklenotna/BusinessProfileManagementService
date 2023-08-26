package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;


public interface ConsumerState {
  void processRequest(BusinessProfileRequest request, BusinessProfileRequestContext businessProfileRequestContext);
}
