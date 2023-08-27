package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfileRequestMapper;
import com.example.BusinessProfileManagement.service.ProfileRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RejectedState implements ConsumerState {
  final ProfileRequestService _profileRequestService;

  public RejectedState(ProfileRequestService _profileRequestService) {
    this._profileRequestService = _profileRequestService;
  }

  @Override
  public void processRequest(BusinessProfileRequest request,
      BusinessProfileRequestContext businessProfileRequestContext) {
    request.setStatus(ApprovalStatus.REJECTED);
    _profileRequestService.updateBusinessProfileRequestEntity(request);
  }
}
