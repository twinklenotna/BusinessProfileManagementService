package com.example.BusinessProfileManagement.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.BusinessProfileManagement.exception.BusinessProfileNotFoundException;
import com.example.BusinessProfileManagement.exception.BusinessProfileRequestNotFoundException;
import com.example.BusinessProfileManagement.helper.ProfileHelper;
import com.example.BusinessProfileManagement.helper.ProfileRequestHelper;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfileRequestResponse;
import com.example.BusinessProfileManagement.model.BusinessProfileUpdateRequest;
import com.example.BusinessProfileManagement.service.BusinessProfileService;
import com.example.BusinessProfileManagement.service.ProfileRequestService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
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
    String profileId = "testProfileId";
    BusinessProfile profile = ProfileHelper.createBusinessProfile(profileId);

    when(profileService.createProfileRequest(profile)).thenReturn(profile);

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

  @Test
  public void testGetProfileUpdateRequests() {
    // Arrange
    String profileId = "123";
    List<BusinessProfileUpdateRequest> requests = ProfileRequestHelper.createBusinessProfileRequests(3, profileId);

    when(profileRequestService.getProfileUpdateRequestsByProfileId(profileId))
        .thenReturn(requests);

    ResponseEntity<List<BusinessProfileUpdateRequest>> response =
        controller.getProfileUpdateRequests(profileId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(requests, response.getBody());
  }

  @Test
  public void testGetProfileUpdateRequest() {
    String profileId = "123";
    String requestId = "456";
    BusinessProfileRequestResponse requestResponse = ProfileRequestHelper.createBusinessProfileRequestResponse(profileId);
    requestResponse.setRequestId(requestId);

    when(profileRequestService.getProfileUpdateRequest(requestId))
        .thenReturn(requestResponse);

    ResponseEntity<BusinessProfileRequestResponse> response =
        controller.getProfileUpdateRequests(profileId, requestId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(requestResponse.getRequestId(), response.getBody().getRequestId());
    assertEquals(requestResponse.getBusinessProfile(), response.getBody().getBusinessProfile());
    assertEquals(requestResponse.getProfileId(), response.getBody().getProfileId());
    assertEquals(requestResponse.getRequestType(), response.getBody().getRequestType());
    assertEquals(requestResponse.getStatus(), response.getBody().getStatus());
    assertEquals(requestResponse.getComments(), response.getBody().getComments());
    assertEquals(requestResponse.getSubscriptionValidations(), response.getBody().getSubscriptionValidations());
  }


  @Test
  public void testGetProfileUpdateRequest_NotFound() {
    String profileId = "123";
    String requestId = "456";

    when(profileRequestService.getProfileUpdateRequest(requestId))
        .thenThrow(new BusinessProfileRequestNotFoundException("request not found"));

    ResponseEntity<BusinessProfileRequestResponse> response =
        controller.getProfileUpdateRequests(profileId, requestId);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}

