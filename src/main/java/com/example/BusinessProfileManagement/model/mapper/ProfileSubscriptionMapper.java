package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.ProfileSubscription;
import com.example.BusinessProfileManagement.model.entity.ProfileSubscriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileSubscriptionMapper {
  ProfileSubscriptionMapper INSTANCE = Mappers.getMapper(ProfileSubscriptionMapper.class);
  ProfileSubscription entityToDto(ProfileSubscriptionEntity entity);

  ProfileSubscriptionEntity dtoToEntity(ProfileSubscription dto);
}
