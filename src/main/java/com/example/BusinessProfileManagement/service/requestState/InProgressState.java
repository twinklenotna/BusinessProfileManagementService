package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.exception.BusinessProfileValidationException;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.service.ProfileValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class InProgressState implements ConsumerState {
  final ProfileValidationService profileValidationService;
  @Override
  public void processRequest(BusinessProfileUpdateRequest request,
                             BusinessProfileRequestContext businessProfileRequestContext) {
    try {
      if (profileValidationService.validateRequest(request)) {
        businessProfileRequestContext.transitionToApproved(request);
      } else {
        businessProfileRequestContext.transitionToRejected(request);
      }
    } catch(BusinessProfileValidationException ex) {
      businessProfileRequestContext.transitionToFailed(request);
    }
  }
}
