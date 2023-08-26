package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.service.ProfileValidationService;
import org.springframework.stereotype.Component;


@Component
public class InProgressState implements ConsumerState {
  final ProfileValidationService _profileValidationService;

  public InProgressState(ProfileValidationService _profileValidationService) {
    this._profileValidationService = _profileValidationService;
  }

  @Override
  public void processRequest(BusinessProfileRequest request,
      BusinessProfileRequestContext businessProfileRequestContext) {
    System.out.println("Request is in progress.");
    try {
      if (_profileValidationService.validateRequest(request)) {
        businessProfileRequestContext.transitionToApproved(request);
      } else {
        businessProfileRequestContext.transitionToRejected(request);
      }
    } catch(BusinessProfileValidationException ex) {
      businessProfileRequestContext.transitionToFailed(request);
    }
  }
}
