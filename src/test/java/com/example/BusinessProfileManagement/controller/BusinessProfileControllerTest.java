package com.example.BusinessProfileManagement.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import com.example.BusinessProfileManagement.service.ProfileRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

public class BusinessProfileControllerTest {

  private BusinessProfileController controller;
  private BusinessProfileService profileService;
  private ProfileRequestService profileRequestService;

  @BeforeEach
  public void setUp() {
    profileService = mock(BusinessProfileService.class);
    profileRequestService = mock(ProfileRequestService.class);
    controller = new BusinessProfileController(profileService, profileRequestService);
  }

  @Test
  public void testUpdateProfile() {
    String profileId = "testProfileId";
    BusinessProfile profile = new BusinessProfile();
    profile.setProfileId(profileId);

    when(profileService.updateProfile(profile)).thenReturn("testRequestId");

    ResponseEntity<String> response = controller.updateProfile(profileId, profile);

    assertEquals(202, response.getStatusCodeValue());
    assertEquals("testRequestId", response.getBody());
  }

  @Test
  public void testCreateProfile() {
    BusinessProfile profile = new BusinessProfile();

    when(profileService.createProfileRequest(profile)).thenReturn("testProfileId");

    ResponseEntity<String> response = controller.createProfile(profile);

    assertEquals(201, response.getStatusCodeValue());
    assertTrue(response.getHeaders().containsKey("Location"));
    assertEquals("/profiles/testProfileId", response.getHeaders().get("Location").get(0));
    assertEquals("testProfileId", response.getBody());
  }

  @Test
  public void testGetProfile() {
    String profileId = "testProfileId";
    BusinessProfile profile = new BusinessProfile();
    profile.setProfileId(profileId);

    when(profileService.getProfileById(profileId)).thenReturn(profile);

    ResponseEntity<BusinessProfile> response = controller.getProfile(profileId);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(profile, response.getBody());
  }

  @Test
  public void testGetProfileNotFound() {
    String profileId = "nonExistentProfileId";

    when(profileService.getProfileById(profileId)).thenThrow(new BusinessProfileNotFoundException("exception"));

    ResponseEntity<BusinessProfile> response = controller.getProfile(profileId);

    assertEquals(404, response.getStatusCodeValue());
    assertNull(response.getBody());
  }

  @Test
  public void testDeleteProfile() {
    String profileId = "testProfileId";

    ResponseEntity<Void> response = controller.deleteProfile(profileId);

    assertEquals(204, response.getStatusCodeValue());
  }

  @Test
  public void testDeleteProfileNotFound() {
    String profileId = "nonExistentProfileId";
    doThrow(new BusinessProfileNotFoundException("profile not found"))
        .when(profileService)
        .deleteProfile(profileId);
    ResponseEntity<Void> response = controller.deleteProfile(profileId);

    assertEquals(404, response.getStatusCodeValue());
    assertNull(response.getBody());
  }

}

