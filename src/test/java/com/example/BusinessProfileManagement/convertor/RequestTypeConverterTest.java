package com.example.BusinessProfileManagement.convertor;

import com.example.BusinessProfileManagement.model.enums.RequestType;
import com.example.BusinessProfileManagement.model.convertor.RequestTypeConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class RequestTypeConverterTest {

  private final RequestTypeConverter converter = new RequestTypeConverter();

  @Test
  public void testConvert() {
    RequestType type = RequestType.CREATE;

    String converted = converter.convert(type);

    assertEquals(type, RequestType.valueOf(converted));
  }

  @Test
  public void testUnconvert() {
    String value = "UPDATE";

    RequestType unconverted = converter.unconvert(value);

    assertEquals(RequestType.UPDATE, unconverted);
  }
}
