package com.example.BusinessProfileManagement.controller;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequest;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import com.example.BusinessProfileManagement.service.ProfileRequestService;
import java.net.URI;
import java.util.List;
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
  private final ProfileRequestService profileRequestService;

  public BusinessProfileController(BusinessProfileService profileService, ProfileRequestService profileRequestService) {
    this.profileService = profileService;
    this.profileRequestService = profileRequestService;
  }

  @PostMapping("/update")
  public ResponseEntity<String> updateProfile(@RequestBody BusinessProfile profile) {
    profileService.updateProfile(profile);
    return ResponseEntity.accepted().body("Profile update request accepted.");
  }

  @PostMapping
  public ResponseEntity<String> createProfile(@RequestBody BusinessProfile profile) {
    String profileId = profileService.createProfileRequest(profile);
    return ResponseEntity.created(URI.create("/profiles/" + profileId))
        .body("Profile creation request is accepted. Profile ID: " + profileId);
  }

  @GetMapping("/{profileId}")
  public ResponseEntity<BusinessProfile> getProfile(@PathVariable String profileId) {
    try{
      BusinessProfile profile = profileService.getProfileById(profileId);
      return ResponseEntity.ok(profile);
    } catch(BusinessProfileNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{profileId}")
  public ResponseEntity<String> deleteProfile(@PathVariable String profileId) {
    try{
      profileService.deleteProfile(profileId);
      return ResponseEntity.ok("Profile deleted");
    } catch(BusinessProfileNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{profileId}/requests")
  public ResponseEntity<List<BusinessProfileRequest>> getProfileRequest(@PathVariable String profileId) {
    List<BusinessProfileRequest> requests = profileRequestService.getProfileRequestByProfileId(profileId);
    return ResponseEntity.ok(requests);
  }

  @GetMapping("/{profileId}/requests/{requestId}")
  public ResponseEntity<BusinessProfileRequest> getProfileRequest(@PathVariable String profileId, @PathVariable String requestId) {
    try{
      BusinessProfileRequest request = profileRequestService.getProfileRequest(requestId);
      return ResponseEntity.ok(request);
    } catch(BusinessProfileRequestNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }
}