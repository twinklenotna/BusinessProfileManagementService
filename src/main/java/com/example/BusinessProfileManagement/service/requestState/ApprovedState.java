package com.example.BusinessProfileManagement.service.requestState;

import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.mapper.BusinessProfilePatchMapper;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import com.example.BusinessProfileManagement.service.ProfileRequestService;
import com.example.BusinessProfileManagement.service.ProfileSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ApprovedState implements ConsumerState {
  private final BusinessProfileService businessProfileService;
  private final ProfileRequestService profileRequestService;
  private final ProfileSubscriptionService profileSubscriptionService;
  private final BusinessProfilePatchMapper businessProfilePatchMapper;
  @Override
  public void processRequest(BusinessProfileUpdateRequest request,
                             BusinessProfileRequestContext businessProfileRequestContext) {
    request.setStatus(ApprovalStatus.APPROVED);
    profileRequestService.updateBusinessProfileRequestEntity(request);
    businessProfileService.updateBusinessProfileEntity(businessProfilePatchMapper.toProfile(request.getBusinessProfile()));
    if(request.getRequestType().equals(RequestType.SUBSCRIBE)) {
      profileSubscriptionService.addSubscriptions(request.getProfileId(), request.getSubscriptions());
    }
  }
}
