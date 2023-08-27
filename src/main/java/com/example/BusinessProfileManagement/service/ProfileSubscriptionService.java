package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.entity.ProfileSubscriptionEntity;
import com.example.BusinessProfileManagement.repository.ProfileSubscriptionRepository;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProfileSubscriptionService {
  Logger logger = LoggerFactory.getLogger(ProfileSubscriptionService.class);
  final BusinessProfileService _businessProfileService;
  final ProfileSubscriptionRepository _profileSubscriptionRepository;

  public ProfileSubscriptionService(BusinessProfileService _businessProfileService,
      ProfileSubscriptionRepository profileSubscriptionRepository) {
    this._businessProfileService = _businessProfileService;
    _profileSubscriptionRepository = profileSubscriptionRepository;
  }

  public void subscribe(ProfileSubscription profileSubscription) {
    BusinessProfile businessProfile = _businessProfileService.getProfileById(profileSubscription.getProfileId());
    _businessProfileService.updateProfile(businessProfile, profileSubscription.getSubscriptions());
  }

  public Set<String> getSubscriptions(String profileId) {
    try{
      ProfileSubscriptionEntity profileSubscriptionEntity = _profileSubscriptionRepository.getProfileById(profileId);
      return profileSubscriptionEntity.getSubscriptions();
    } catch(NullPointerException ex) {
      logger.warn("No subscriptions found for profileId: "+ profileId);
      return new HashSet<>();
    }
  }

  public void addSubscriptions(String profileId, Set<String> subscriptions) {
    ProfileSubscriptionEntity profileSubscriptionEntity = new ProfileSubscriptionEntity(profileId, new HashSet<>());
    try{
      profileSubscriptionEntity = _profileSubscriptionRepository.getProfileById(profileId);
      profileSubscriptionEntity.getSubscriptions().addAll(subscriptions);
    } catch(NullPointerException ex) {
      logger.warn("No subscriptions found for profileId: "+ profileId);
    } finally {
      _profileSubscriptionRepository.save(profileSubscriptionEntity);
    }
  }
}
