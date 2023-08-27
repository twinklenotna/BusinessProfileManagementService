package com.example.BusinessProfileManagement.model.mapper;

import com.example.BusinessProfileManagement.model.BusinessProfile;
import com.example.BusinessProfileManagement.model.TaxInfo;
import com.example.BusinessProfileManagement.model.entity.BusinessProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface BusinessProfileMapper {
  @Mappings({
      @Mapping(source = "taxInfoEntity", target = "taxInfo"),
  })
  BusinessProfile entityToDto(BusinessProfileEntity entity);

  @Mappings({
      @Mapping(source = "taxInfo", target = "taxInfoEntity"),
  })
  BusinessProfileEntity dtoToEntity(BusinessProfile dto);
}
