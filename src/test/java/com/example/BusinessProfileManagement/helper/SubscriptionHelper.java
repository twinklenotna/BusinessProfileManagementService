package com.example.BusinessProfileManagement.helper;

import com.example.BusinessProfileManagement.model.ProfileSubscription;
import java.util.HashSet;
import java.util.Set;


public class SubscriptionHelper {
  public static ProfileSubscription createProfileSubscription(String profileId) {
    Set<String> subscriptions = new HashSet<>();
    subscriptions.add("Subscription1");
    subscriptions.add("Subscription2");
    return new ProfileSubscription(profileId, subscriptions);
  }
}
