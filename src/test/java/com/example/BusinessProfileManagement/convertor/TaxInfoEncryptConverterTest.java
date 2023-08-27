package com.example.BusinessProfileManagement.convertor;

import com.example.BusinessProfileManagement.exception.DataEncryptionException;
import com.example.BusinessProfileManagement.model.TaxInfo;
import com.example.BusinessProfileManagement.model.convertor.TaxInfoEncryptConverter;
import javax.crypto.IllegalBlockSizeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.util.Base64;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaxInfoEncryptConverterTest {

  private TaxInfoEncryptConverter converter;

  @BeforeEach
  public void setUp() throws NoSuchAlgorithmException {
    converter = new TaxInfoEncryptConverter();
  }

  @Test
  public void testConvertAndUnconvert() {
    // Create a TaxInfo object for testing
    TaxInfo originalTaxInfo = new TaxInfo("Tax123", "USD");

    // Convert the TaxInfo object to a string
    String encryptedData = converter.convert(originalTaxInfo);

    // Unconvert the string back to a TaxInfo object
    TaxInfo decryptedTaxInfo = converter.unconvert(encryptedData);

    // Ensure that the decrypted TaxInfo matches the original
    assertEquals(originalTaxInfo, decryptedTaxInfo);
  }

}

