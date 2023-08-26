package com.example.BusinessProfileManagement.model.convertor;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.example.BusinessProfileManagement.config.BusinessProfileConfig;
import com.example.BusinessProfileManagement.exception.DataEncryptionException;
import com.example.BusinessProfileManagement.model.TaxInfo;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.example.BusinessProfileManagement.config.BusinessProfileConfig.*;

public class TaxInfoEncryptConverter implements DynamoDBTypeConverter<String, TaxInfo> {
  private final SecretKey secretKey;

  public TaxInfoEncryptConverter() throws NoSuchAlgorithmException {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128);
    secretKey = keyGenerator.generateKey();
  }

  @Override
  public String convert(TaxInfo taxInfo) {
    try {
      return encrypt(taxInfo.toJson(), encryptionKey);
    } catch (Exception e) {
      throw new DataEncryptionException("unable to encrypt tax info", e);
    }
  }

  @Override
  public TaxInfo unconvert(String encryptedData) {
    try {
      return TaxInfo.fromJson(decrypt(encryptedData, encryptionKey));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private String encrypt(String data, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] encryptedData = cipher.doFinal(data.getBytes());
    return Base64.getEncoder().encodeToString(encryptedData);
  }

  private String decrypt(String encryptedData, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, secretKey);
    byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
    return new String(decryptedData);
  }
}
