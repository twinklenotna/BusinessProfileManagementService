package com.example.BusinessProfileManagement.model;

import com.example.BusinessProfileManagement.model.enums.Product;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class ProfileUpdateRequest {
  private String profileId;
  private String companyName;
  private String legalName;
  private Address businessAddress;
  private Address legalAddress;
  private String pan;
  private String ein;
  private String email;
  private String website;
  private Set<Product> subscriptions;
}
