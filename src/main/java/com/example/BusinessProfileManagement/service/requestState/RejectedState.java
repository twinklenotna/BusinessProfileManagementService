package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.service.BusinessProfileRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RejectedState implements ConsumerState {
  final private BusinessProfileRequestService _businessProfileRequestService;

  @Override
  public void processRequest(BusinessProfileUpdateRequest request,
                             BusinessProfileRequestContext businessProfileRequestContext) {
    request.setStatus(ApprovalStatus.REJECTED);
    _businessProfileRequestService.updateBusinessProfileRequestEntity(request);
  }
}
