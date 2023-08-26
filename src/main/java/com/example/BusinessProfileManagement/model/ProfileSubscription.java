package com.example.BusinessProfileManagement.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileSubscription {
  private String profileId;
  private Set<String> subscriptions;
}
