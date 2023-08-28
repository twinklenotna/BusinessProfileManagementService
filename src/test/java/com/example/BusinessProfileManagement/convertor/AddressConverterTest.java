package com.example.BusinessProfileManagement.convertor;

import com.example.BusinessProfileManagement.model.convertor.AddressConverter;
import com.example.BusinessProfileManagement.model.entity.AddressEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class AddressConverterTest {

  private final AddressConverter addressConverter = new AddressConverter();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testConvert() throws JsonProcessingException {
    AddressEntity addressEntity = new AddressEntity("123 Main St", "line2", "City", "state","21334","india");

    String json = addressConverter.convert(addressEntity);

    assertEquals(addressEntity, objectMapper.readValue(json, AddressEntity.class));
  }

  @Test
  public void testUnconvert() throws JsonProcessingException {
    AddressEntity addressEntity = new AddressEntity("123 Main St", "line2", "City", "state","21334","india");

    String json = objectMapper.writeValueAsString(addressEntity);

    AddressEntity unconverted = addressConverter.unconvert(json);

    assertEquals(addressEntity, unconverted);
  }

  @Test
  public void testConvertWithJsonProcessingException() throws JsonProcessingException {
    AddressEntity addressEntity = Mockito.mock(AddressEntity.class);

    addressConverter.objectMapper = Mockito.mock(ObjectMapper.class);
    Mockito.when(addressConverter.objectMapper.writeValueAsString(Mockito.any())).thenThrow(JsonProcessingException.class);

    assertThrows(IllegalArgumentException.class, () -> addressConverter.convert(addressEntity));
  }

  @Test
  public void testUnconvertWithJsonProcessingException() {
    String invalidJson = "{invalidJson}";

    assertThrows(IllegalArgumentException.class, () -> addressConverter.unconvert(invalidJson));
  }
}
