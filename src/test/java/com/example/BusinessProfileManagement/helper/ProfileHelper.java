package com.example.BusinessProfileManagement.helper;

import com.example.BusinessProfileManagement.model.Address;
import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.BusinessProfilePatchRequest;
import com.example.BusinessProfileManagement.model.TaxInfo;
import com.example.BusinessProfileManagement.model.entity.AddressEntity;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import java.util.HashSet;
import java.util.Set;

public class ProfileHelper {
  public static BusinessProfile createBusinessProfile(String profileId) {
    BusinessProfile businessProfile = new BusinessProfile();
    businessProfile.setProfileId(profileId);
    businessProfile.setCompanyName("Sample Company");
    businessProfile.setLegalName("Sample Legal Name");

    Address businessAddress = new Address();
    businessAddress.setAddressLine1("123 Main St");
    businessAddress.setCity("Sample City");
    businessAddress.setZip("12345");
    businessProfile.setBusinessAddress(businessAddress);

    Address legalAddress = new Address();
    legalAddress.setAddressLine1("456 Legal St");
    legalAddress.setCity("Legal City");
    legalAddress.setZip("54321");
    businessProfile.setLegalAddress(legalAddress);

    TaxInfo taxInfo = new TaxInfo();
    taxInfo.setEin("123-45-6789");
    taxInfo.setPan("123-45-6789");
    businessProfile.setTaxInfo(taxInfo);

    businessProfile.setEmail("sample@example.com");
    businessProfile.setWebsite("https://www.example.com");
    businessProfile.setStatus(ProfileStatus.ACTIVE);

    return businessProfile;
  }

  public static BusinessProfilePatchRequest createBusinessProfilePatchRequest(String profileId) {
    BusinessProfilePatchRequest businessProfile = new BusinessProfilePatchRequest();
    businessProfile.setProfileId(profileId);
    businessProfile.setCompanyName("Sample Company");
    businessProfile.setLegalName("Sample Legal Name");

    Address businessAddress = new Address();
    businessAddress.setAddressLine1("123 Main St");
    businessAddress.setCity("Sample City");
    businessAddress.setZip("12345");
    businessProfile.setBusinessAddress(businessAddress);

    Address legalAddress = new Address();
    legalAddress.setAddressLine1("456 Legal St");
    legalAddress.setCity("Legal City");
    legalAddress.setZip("54321");
    businessProfile.setLegalAddress(legalAddress);

    TaxInfo taxInfo = new TaxInfo();
    taxInfo.setEin("123-45-6789");
    taxInfo.setPan("123-45-6789");
    businessProfile.setTaxInfo(taxInfo);

    businessProfile.setEmail("sample@example.com");
    businessProfile.setWebsite("https://www.example.com");
    businessProfile.setStatus(ProfileStatus.ACTIVE);

    return businessProfile;
  }


  public static BusinessProfileEntity createBusinessProfileEntity(String profileId) {
    BusinessProfileEntity businessProfile = new BusinessProfileEntity();
    businessProfile.setProfileId(profileId);
    businessProfile.setCompanyName("Sample Company");
    businessProfile.setLegalName("Sample Legal Name");

    AddressEntity businessAddress = new AddressEntity();
    businessAddress.setAddressLine1("123 Main St");
    businessAddress.setCity("Sample City");
    businessAddress.setZip("12345");
    businessProfile.setBusinessAddress(businessAddress);

    AddressEntity legalAddress = new AddressEntity();
    legalAddress.setAddressLine1("456 Legal St");
    legalAddress.setCity("Legal City");
    legalAddress.setZip("54321");
    businessProfile.setLegalAddress(legalAddress);

    TaxInfo taxInfo = new TaxInfo();
    taxInfo.setEin("123-45-6789");
    taxInfo.setPan("123-45-6789");
    businessProfile.setTaxInfoEntity(taxInfo);

    businessProfile.setEmail("sample@example.com");
    businessProfile.setWebsite("https://www.example.com");
    businessProfile.setStatus(ProfileStatus.ACTIVE);

    return businessProfile;
  }
}
