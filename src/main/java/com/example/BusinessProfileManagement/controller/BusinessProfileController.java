package com.example.BusinessProfileManagement.controller;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileStatusResponse;
import com.example.BusinessProfileManagement.model.ProfileUpdateRequest;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import java.util.concurrent.CompletableFuture;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/profiles")
public class BusinessProfileController {
  private final BusinessProfileService profileService;

  public BusinessProfileController(BusinessProfileService profileService) {
    this.profileService = profileService;
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateProfile(@RequestBody ProfileUpdateRequest profile) {
    // Start the update process for each product
    CompletableFuture<Void> updateTask = profileService.updateProfileAsync(profile);

    // Return an immediate response to the client
    return ResponseEntity.accepted().body("Profile update request accepted.");

  }

  @PostMapping
  public BusinessProfile createProfile(@RequestBody BusinessProfile profile) {
    return profileService.saveProfile(profile);
  }

  @GetMapping("/{profileId}")
  public BusinessProfileStatusResponse getProfile(@PathVariable String profileId) {
    return profileService.getProfileById(profileId);
  }

  @DeleteMapping("/{profileId}")
  public ResponseEntity<String> deleteProfile(@PathVariable String profileId) {
    profileService.deleteProfile(profileId);
    return ResponseEntity.ok().body("Profile update request accepted.");
  }

  // Other CRUD endpoints
}