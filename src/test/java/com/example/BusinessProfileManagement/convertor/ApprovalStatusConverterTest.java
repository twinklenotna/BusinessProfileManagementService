package com.example.BusinessProfileManagement.convertor;

import com.example.BusinessProfileManagement.model.convertor.ApprovalStatusConverter;
import com.example.BusinessProfileManagement.model.enums.ApprovalStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class ApprovalStatusConverterTest {

  private final ApprovalStatusConverter converter = new ApprovalStatusConverter();

  @Test
  public void testConvert() {
    ApprovalStatus status = ApprovalStatus.APPROVED;

    String converted = converter.convert(status);

    assertEquals(status, ApprovalStatus.valueOf(converted));
  }

  @Test
  public void testUnconvert() {
    String value = "REJECTED";

    ApprovalStatus unconverted = converter.unconvert(value);

    assertEquals(ApprovalStatus.REJECTED, unconverted);
  }
}
