package com.example.BusinessProfileManagement.controller;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.service.BusinessProfileRequestService;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import java.net.URI;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class BusinessProfileController {
  private final BusinessProfileService profileService;
  private final BusinessProfileRequestService businessProfileRequestService;

  @PostMapping
  public ResponseEntity<BusinessProfile> createProfile(@RequestBody BusinessProfile profile) {
    BusinessProfile profileCreated = profileService.createProfileRequest(profile);
    return ResponseEntity.created(URI.create("/profile/" + profileCreated.getProfileId()))
            .body(profileCreated);
  }

  @PatchMapping("/{profileId}")
  public ResponseEntity<BusinessProfileUpdateRequest> updateProfile(@PathVariable String profileId, @RequestBody BusinessProfilePatchRequest profile) {
    profile.setProfileId(profileId);
    BusinessProfileUpdateRequest request = profileService.updateProfile(profile);
    return ResponseEntity.accepted().body(request);
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
  public ResponseEntity<Void> deleteProfile(@PathVariable String profileId) {
    try {
      profileService.deleteProfile(profileId);
      return ResponseEntity.noContent().build();
    } catch (BusinessProfileNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{profileId}/requests")
  public ResponseEntity<List<BusinessProfileUpdateRequest>> getProfileUpdateRequests(@PathVariable String profileId) {
    List<BusinessProfileUpdateRequest> requests = businessProfileRequestService.getProfileUpdateRequestsByprofileId(profileId);
    return ResponseEntity.ok(requests);
  }

  @GetMapping("/{profileId}/requests/{requestId}")
  public ResponseEntity<BusinessProfileRequestResponse> getProfileUpdateRequests(@PathVariable String profileId, @PathVariable String requestId) {
    try{
      BusinessProfileRequestResponse request = businessProfileRequestService.getProfileUpdateRequest(requestId);
      return ResponseEntity.ok(request);
    } catch(BusinessProfileRequestNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }
}