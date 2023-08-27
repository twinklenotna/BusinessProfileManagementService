package com.example.BusinessProfileManagement.controller;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.service.ProfileSubscriptionService;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
  final ProfileSubscriptionService _profileSubscriptionService;

  public SubscriptionController(ProfileSubscriptionService _profileSubscriptionService) {
    this._profileSubscriptionService = _profileSubscriptionService;
  }

  @PostMapping("/subscribe")
  public ResponseEntity<String> subscribe(@RequestBody ProfileSubscription profileSubscription) {
    _profileSubscriptionService.subscribe(profileSubscription);
    return ResponseEntity.accepted().body("Profile subscription request accepted.");
  }

  @GetMapping("/{profileId}")
  public ResponseEntity<Set<String>> getSubscriptions(@PathVariable String profileId) {
    Set<String> subscriptions = _profileSubscriptionService.getSubscriptions(profileId);
    return ResponseEntity.ok(subscriptions);
  }
}
