package com.example.BusinessProfileManagement.model;

import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProfilePatchRequest {
  @NonNull
  private String profileId;
  private String companyName;
  private String legalName;
  private Address businessAddress;
  private Address legalAddress;
  private TaxInfo taxInfo;
  private String email;
  private String website;
  private ProfileStatus status;
}
