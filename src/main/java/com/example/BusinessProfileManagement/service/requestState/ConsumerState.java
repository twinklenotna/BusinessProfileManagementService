package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;


public interface ConsumerState {
  void processRequest(BusinessProfileUpdateRequest request, BusinessProfileRequestContext businessProfileRequestContext);
}
