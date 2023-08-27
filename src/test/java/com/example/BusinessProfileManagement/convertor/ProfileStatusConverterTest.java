package com.example.BusinessProfileManagement.convertor;

import com.example.BusinessProfileManagement.model.convertor.ProfileStatusConverter;
import com.example.BusinessProfileManagement.model.enums.ProfileStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ProfileStatusConverterTest {

  private final ProfileStatusConverter converter = new ProfileStatusConverter();

  @Test
  public void testConvert() {
    ProfileStatus status = ProfileStatus.ACTIVE;

    String converted = converter.convert(status);

    assertEquals(status, ProfileStatus.valueOf(converted));
  }

  @Test
  public void testUnconvert() {
    String value = "INACTIVE";

    ProfileStatus unconverted = converter.unconvert(value);

    assertEquals(ProfileStatus.INACTIVE, unconverted);
  }
}
