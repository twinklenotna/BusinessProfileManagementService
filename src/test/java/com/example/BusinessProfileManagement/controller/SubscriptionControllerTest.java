package com.example.BusinessProfileManagement.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.service.ProfileSubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import java.util.HashSet;
import java.util.Set;

public class SubscriptionControllerTest {

  private SubscriptionController controller;
  private ProfileSubscriptionService profileSubscriptionService;

  @BeforeEach
  public void setUp() {
    profileSubscriptionService = mock(ProfileSubscriptionService.class);
    controller = new SubscriptionController(profileSubscriptionService);
  }

  @Test
  public void testSubscribe() {
    ProfileSubscription profileSubscription = new ProfileSubscription();

    ResponseEntity<String> response = controller.subscribe(profileSubscription);

    assertEquals(202, response.getStatusCodeValue());
    assertEquals("Profile subscription request accepted.", response.getBody());

    verify(profileSubscriptionService, times(1)).subscribe(profileSubscription);
  }

  @Test
  public void testGetSubscriptions() {
    String profileId = "testProfileId";
    Set<String> subscriptions = new HashSet<>();
    subscriptions.add("subscription1");
    subscriptions.add("subscription2");

    when(profileSubscriptionService.getSubscriptions(profileId)).thenReturn(subscriptions);

    ResponseEntity<Set<String>> response = controller.getSubscriptions(profileId);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(subscriptions, response.getBody());

    verify(profileSubscriptionService, times(1)).getSubscriptions(profileId);
  }
}

