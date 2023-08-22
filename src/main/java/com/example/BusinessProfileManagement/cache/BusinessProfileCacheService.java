package com.example.BusinessProfileManagement.cache;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class BusinessProfileCacheService {
  private final CacheManager cacheManager;

  public BusinessProfileCacheService(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @CachePut(value = "businessProfileCache", key = "'profileID-' + #profileId + '-' + #status")
  public BusinessProfile storeProfile(String profileId, ApprovalStatus status, BusinessProfile profile) {
    profile.setApprovalStatus(status);
    // Save the profile to your data source
    // ...
    return profile;
  }

  @CacheEvict(value = "businessProfileCache", key = "'profileID-' + #profileId + '-IN_PROGRESS'")
  public void removeProfileInProgress(String profileId) {
    // Remove the profile with IN_PROGRESS status from the cache
  }

  public BusinessProfile getProfileFromCache(String profileId, ApprovalStatus status) {
    Cache cache = cacheManager.getCache("businessProfileCache");
    if (cache != null) {
      String cacheKey = "profileID-" + profileId + "-" + status;
      Cache.ValueWrapper valueWrapper = cache.get(cacheKey);

      if (valueWrapper != null) {
        return (BusinessProfile) valueWrapper.get();
      }
    }
    return null; // Profile not found in the cache
  }
}

