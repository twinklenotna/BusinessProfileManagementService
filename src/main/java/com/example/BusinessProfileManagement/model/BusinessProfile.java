package com.example.BusinessProfileManagement.model;

import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProfile {
  @NonNull
  private String profileId;
  @NonNull
  private String companyName;
  @NonNull
  private String legalName;
  private Address businessAddress;
  private Address legalAddress;
  private TaxInfo taxInfo;
  @NonNull
  private String email;
  @NonNull
  private String website;
  private ProfileStatus status;

  public void applyPatch(BusinessProfilePatchRequest patch) {
    Optional.ofNullable(patch.getCompanyName()).ifPresent(value -> this.companyName = value);
    Optional.ofNullable(patch.getLegalName()).ifPresent(value -> this.legalName = value);
    Optional.ofNullable(patch.getBusinessAddress()).ifPresent(value -> this.businessAddress = value);
    Optional.ofNullable(patch.getLegalAddress()).ifPresent(value -> this.legalAddress = value);
    Optional.ofNullable(patch.getTaxInfo()).ifPresent(value -> this.taxInfo = value);
    Optional.ofNullable(patch.getEmail()).ifPresent(value -> this.email = value);
    Optional.ofNullable(patch.getWebsite()).ifPresent(value -> this.website = value);
    Optional.ofNullable(patch.getStatus()).ifPresent(value -> this.status = value);
  }
}
