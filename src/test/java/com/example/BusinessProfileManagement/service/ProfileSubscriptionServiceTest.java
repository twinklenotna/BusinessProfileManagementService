package com.example.BusinessProfileManagement.service;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.helper.SubscriptionHelper;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.entity.ProfileSubscriptionEntity;
import com.example.BusinessProfileManagement.model.mapper.ProfileSubscriptionMapper;
import com.example.BusinessProfileManagement.repository.ProfileSubscriptionRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileSubscriptionServiceTest {

  @InjectMocks
  private ProfileSubscriptionService profileSubscriptionService;

  @Mock
  private BusinessProfileService businessProfileService;

  @Mock
  private ProfileSubscriptionRepository profileSubscriptionRepository;
  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSubscribe() {
    ProfileSubscription profileSubscription = new ProfileSubscription();

    when(businessProfileService.getProfileById(any())).thenReturn(new BusinessProfile());

    profileSubscriptionService.subscribe(profileSubscription);
  }

  @Test
  public void testGetSubscriptions() {

    String profileId = "123";
    ProfileSubscription profileSubscription = SubscriptionHelper.createProfileSubscription(profileId);

    when(profileSubscriptionRepository.getProfileById(profileId)).thenReturn(
        ProfileSubscriptionMapper.INSTANCE.dtoToEntity(profileSubscription));

    Set<String> subscriptions = profileSubscriptionService.getSubscriptions(profileId);

    assertNotNull(subscriptions);
    assertEquals(subscriptions, profileSubscription.getSubscriptions());
  }

  @Test
  public void testGetSubscriptionsException() {
    String profileId = "123";
    when(profileSubscriptionRepository.getProfileById(profileId))
        .thenThrow(new NullPointerException());

    Set<String> subscriptions = profileSubscriptionService.getSubscriptions(profileId);

    assertNotNull(subscriptions);
    assertEquals(subscriptions.size(), 0);
  }

  @Test
  public void testAddSubscriptions() {
    String profileId = "123";
    Set<String> subscriptions = new HashSet<>(Arrays.asList("product1", "product2"));
    ProfileSubscriptionEntity profileSubscriptionEntity = new ProfileSubscriptionEntity();
    profileSubscriptionEntity.setSubscriptions(new HashSet<>(Arrays.asList("existingProduct")));
    profileSubscriptionEntity.setProfileId(profileId);

    ProfileSubscriptionEntity profileSubscriptionEntityUpdated = new ProfileSubscriptionEntity();
    profileSubscriptionEntityUpdated.setSubscriptions(new HashSet<>(Arrays.asList("existingProduct")));
    profileSubscriptionEntityUpdated.setProfileId(profileId);
    profileSubscriptionEntityUpdated.getSubscriptions().addAll(subscriptions);

    when(profileSubscriptionRepository.getProfileById(profileId)).thenReturn(profileSubscriptionEntity);
    when(profileSubscriptionRepository.save(any())).thenReturn(profileSubscriptionEntityUpdated);

    Set<String> updatedSubscriptions = profileSubscriptionService.addSubscriptions(profileId, subscriptions);
    assertTrue(updatedSubscriptions.containsAll(subscriptions));
  }

  @Test
  public void testAddSubscriptionsException() {

    String profileId = "123";
    Set<String> subscriptions = new HashSet<>(Arrays.asList("product1", "product2"));
    ProfileSubscriptionEntity profileSubscriptionEntity = new ProfileSubscriptionEntity();
    profileSubscriptionEntity.setSubscriptions(new HashSet<>(Arrays.asList("existingProduct")));

    when(profileSubscriptionRepository.getProfileById(profileId)).thenThrow(new NullPointerException());
    when(profileSubscriptionRepository.save(any())).thenReturn(profileSubscriptionEntity);

    profileSubscriptionService.addSubscriptions(profileId, subscriptions);

    verify(profileSubscriptionRepository, times(1)).save(any());
    verify(profileSubscriptionRepository, times(1)).getProfileById(any());
  }

}

