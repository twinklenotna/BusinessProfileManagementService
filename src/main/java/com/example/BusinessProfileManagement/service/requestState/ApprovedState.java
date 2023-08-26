package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import com.example.BusinessProfileManagement.service.ProfileRequestService;
import com.example.BusinessProfileManagement.service.ProfileSubscriptionService;
import org.springframework.stereotype.Component;


@Component
public class ApprovedState implements ConsumerState {
  final BusinessProfileService _businessProfileService;
  final ProfileRequestService _profileRequestService;
  final ProfileSubscriptionService _profileSubscriptionService;

  public ApprovedState(BusinessProfileService _businessProfileService, ProfileRequestService _profileRequestService,
      ProfileSubscriptionService _profileSubscriptionService) {
    this._businessProfileService = _businessProfileService;
    this._profileRequestService = _profileRequestService;
    this._profileSubscriptionService = _profileSubscriptionService;
  }

  @Override
  public void processRequest(BusinessProfileRequest request,
      BusinessProfileRequestContext businessProfileRequestContext) {
    request.setStatus(ApprovalStatus.APPROVED);
    _profileRequestService.updateBusinessProfileRequestEntity(request);
    _businessProfileService.updateBusinessProfileEntity(request.getBusinessProfile());
    if(request.getRequestType().equals(RequestType.SUBSCRIBE)) {
      ProfileSubscription profileSubscription =
          new ProfileSubscription(request.getProfileId(), request.getSubscriptions());
      _profileSubscriptionService.subscribe(profileSubscription);
    }
  }
}