package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.entity.ProfileSubscriptionEntity;
import com.example.BusinessProfileManagement.repository.ProfileSubscriptionRepository;
import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileSubscriptionService {
  Logger logger = LoggerFactory.getLogger(ProfileSubscriptionService.class);
  private final BusinessProfileService businessProfileService;
  private final ProfileSubscriptionRepository profileSubscriptionRepository;

  public void subscribe(ProfileSubscription profileSubscription) {
    BusinessProfile businessProfile = businessProfileService.getProfileById(profileSubscription.getProfileId());
    businessProfileService.updateProfile(businessProfile, profileSubscription.getSubscriptions());
  }

  public Set<String> getSubscriptions(String profileId) {
    try{
      ProfileSubscriptionEntity profileSubscriptionEntity = profileSubscriptionRepository.getProfileById(profileId);
      return profileSubscriptionEntity.getSubscriptions();
    } catch(NullPointerException ex) {
      logger.warn("No subscriptions found for profileId: "+ profileId);
      return new HashSet<>();
    }
  }

  public void addSubscriptions(String profileId, Set<String> subscriptions) {
    ProfileSubscriptionEntity profileSubscriptionEntity = new ProfileSubscriptionEntity(profileId, new HashSet<>());
    try{
      profileSubscriptionEntity = profileSubscriptionRepository.getProfileById(profileId);
      profileSubscriptionEntity.getSubscriptions().addAll(subscriptions);
    } catch(NullPointerException ex) {
      logger.warn("No subscriptions found for profileId: "+ profileId);
    } finally {
      profileSubscriptionRepository.save(profileSubscriptionEntity);
    }
  }
}
