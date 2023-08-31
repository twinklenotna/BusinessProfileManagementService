package com.example.BusinessProfileManagement.controller;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import com.example.BusinessProfileManagement.service.ProfileRequestService;
import java.net.URI;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
  private final ProfileRequestService profileRequestService;


  @PostMapping
  public ResponseEntity<String> createProfile(@RequestBody BusinessProfile profile) {
    String profileId = profileService.createProfileRequest(profile).getProfileId();
    return ResponseEntity.created(URI.create("/profiles/" + profileId))
            .body(profileId);
  }

  @PutMapping("/{profileId}")
  public ResponseEntity<String> updateProfile(@PathVariable String profileId, @RequestBody BusinessProfile profile) {
    profile.setProfileId(profileId);
    String requestId = profileService.updateProfile(profile);
    return ResponseEntity.accepted().body(requestId);
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
    List<BusinessProfileUpdateRequest> requests = profileRequestService.getProfileUpdateRequestsByProfileId(profileId);
    return ResponseEntity.ok(requests);
  }

  @GetMapping("/{profileId}/requests/{requestId}")
  public ResponseEntity<BusinessProfileRequestResponse> getProfileUpdateRequests(@PathVariable String profileId, @PathVariable String requestId) {
    try{
      BusinessProfileRequestResponse request = profileRequestService.getProfileUpdateRequest(requestId);
      return ResponseEntity.ok(request);
    } catch(BusinessProfileRequestNotFoundException ex) {
      return ResponseEntity.notFound().build();
    }
  }
}