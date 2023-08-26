package com.example.BusinessProfileManagement.model;

import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProfile {
  private String profileId;
  private String companyName;
  private String legalName;
  private Address businessAddress;
  private Address legalAddress;
  private TaxInfo taxInfo;
  private String email;
  private String website;
  private ProfileStatus status;

  public void applyPatch(BusinessProfile patch) {
    if (patch.getCompanyName() != null) {
      this.companyName = patch.getCompanyName();
    }
    if (patch.getLegalName() != null) {
      this.legalName = patch.getLegalName();
    }
    if (patch.getBusinessAddress() != null) {
      this.businessAddress = patch.getBusinessAddress();
    }
    if (patch.getLegalAddress() != null) {
      this.legalAddress = patch.getLegalAddress();
    }
    if (patch.getTaxInfo() != null) {
      this.taxInfo = patch.getTaxInfo();
    }
    if (patch.getEmail() != null) {
      this.email = patch.getEmail();
    }
    if (patch.getWebsite() != null) {
      this.website = patch.getWebsite();
    }
    if (patch.getStatus() != null) {
      this.status = patch.getStatus();
    }
  }
}
