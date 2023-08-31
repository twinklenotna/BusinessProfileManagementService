package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.entity.ProfileSubscriptionEntity;
import com.example.BusinessProfileManagement.repository.ProfileSubscriptionRepository;
import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileSubscriptionService {
  private final BusinessProfileService businessProfileService;
  private final ProfileSubscriptionRepository profileSubscriptionRepository;

  public void subscribe(ProfileSubscription profileSubscription) {
    BusinessProfile businessProfile = businessProfileService.getProfileById(profileSubscription.getProfileId());
    businessProfileService.updateProfile(businessProfile, profileSubscription.getSubscriptions());
  }

  /**
   * To get the subscriptions for a profile from Subscriptions Service
   * @param profileId profileId
   * @return Set of subscriptions
   */
  @Cacheable(value = "subscriptions", key = "#profileId")
  public Set<String> getSubscriptions(String profileId) {
    try{
      ProfileSubscriptionEntity profileSubscriptionEntity = profileSubscriptionRepository.getProfileById(profileId);
      return profileSubscriptionEntity.getSubscriptions();
    } catch(NullPointerException ex) {
      log.warn("No subscriptions found for profileId: "+ profileId);
      return new HashSet<>();
    }
  }

  /**
   * To add subscriptions for a profile
   * @param profileId profileId
   * @param subscriptions subscriptions
   * @return Set of subscriptions
   */
  @CachePut(value = "subscriptions", key = "#profileId")
  public Set<String>  addSubscriptions(String profileId, Set<String> subscriptions) {
    ProfileSubscriptionEntity profileSubscriptionEntity = new ProfileSubscriptionEntity(profileId, new HashSet<>(subscriptions));
    try{
      ProfileSubscriptionEntity profileSubscriptions= profileSubscriptionRepository.getProfileById(profileId);
      if(profileSubscriptions != null) {
        profileSubscriptionEntity.getSubscriptions().addAll(profileSubscriptions.getSubscriptions());
      }
    } catch(NullPointerException ex) {
      log.warn("No subscriptions found for profileId: "+ profileId);
    } finally {
      log.info("Adding subscription for profile: {} ", profileId);
      subscriptions = profileSubscriptionRepository.save(profileSubscriptionEntity).getSubscriptions();
    }
    return subscriptions;
  }
}
