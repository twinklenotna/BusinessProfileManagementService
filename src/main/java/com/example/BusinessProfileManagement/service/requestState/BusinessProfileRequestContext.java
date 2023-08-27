package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class BusinessProfileRequestContext {
  private ConsumerState currentState;
  private final ConsumerState approvedState;
  private final ConsumerState failedState;
  private final ConsumerState rejectedState;
  private final ConsumerState inProgressState;

  public BusinessProfileRequestContext(@Qualifier("approvedState") ConsumerState approvedState,
      @Qualifier("failedState") ConsumerState failedState, @Qualifier("rejectedState") ConsumerState rejectedState,
      @Qualifier("inProgressState") ConsumerState inProgessState) {
    currentState = inProgessState;
    this.approvedState = approvedState;
    this.failedState = failedState;
    this.rejectedState = rejectedState;
    this.inProgressState = inProgessState;
  }

  @Transactional
  public void processRequest(BusinessProfileRequest request) {
    currentState = inProgressState;
    currentState.processRequest(request, this);
  }

  public void transitionToApproved(BusinessProfileRequest request) {
    currentState = approvedState;
    currentState.processRequest(request, this);
  }

  public void transitionToRejected(BusinessProfileRequest request) {
    currentState = rejectedState;
    currentState.processRequest(request, this);
  }

  public void transitionToFailed(BusinessProfileRequest request) {
    currentState = failedState;
    currentState.processRequest(request, this);
  }
}
