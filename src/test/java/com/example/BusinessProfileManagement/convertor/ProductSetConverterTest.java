package com.example.BusinessProfileManagement.convertor;

import com.example.BusinessProfileManagement.model.convertor.ProductSetConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ProductSetConverterTest {

  private final ProductSetConverter converter = new ProductSetConverter();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void testConvert() throws JsonProcessingException {
    Set<String> products = new HashSet<>();
    products.add("Product1");
    products.add("Product2");

    String json = converter.convert(products);

    assertEquals(products, objectMapper.readValue(json, new TypeReference<Set<String>>() {}));
  }

  @Test
  public void testUnconvert() throws JsonProcessingException {
    Set<String> products = new HashSet<>();
    products.add("Product1");
    products.add("Product2");

    String json = objectMapper.writeValueAsString(products);

    Set<String> unconverted = converter.unconvert(json);

    assertEquals(products, unconverted);
  }
}
