package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.properties.Property;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YggdrasilServicesKeyInfo implements ServicesKeyInfo {
   private static final Logger LOGGER = LoggerFactory.getLogger(YggdrasilServicesKeyInfo.class);
   private static final int KEY_SIZE_BITS = 4096;
   private static final String KEY_ALGORITHM = "RSA";
   private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
   private final PublicKey publicKey;

   private YggdrasilServicesKeyInfo(PublicKey publicKey) {
      this.publicKey = publicKey;
      String algorithm = publicKey.getAlgorithm();
      if (!algorithm.equals("RSA")) {
         throw new IllegalArgumentException("Expected RSA key, got " + algorithm);
      }
   }

   public static ServicesKeyInfo createFromResources() {
      try {
         byte[] keyBytes = YggdrasilServicesKeyInfo.class.getResourceAsStream("/yggdrasil_session_pubkey.der").readAllBytes();
         X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
         KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         PublicKey publicKey = keyFactory.generatePublic(spec);
         return new YggdrasilServicesKeyInfo(publicKey);
      } catch (Exception var4) {
         throw new AssertionError("Missing/invalid yggdrasil public key!", var4);
      }
   }

   @Override
   public Signature signature() {
      try {
         Signature signature = Signature.getInstance("SHA1withRSA");
         signature.initVerify(this.publicKey);
         return signature;
      } catch (InvalidKeyException | NoSuchAlgorithmException var2) {
         throw new AssertionError("Failed to create signature", var2);
      }
   }

   @Override
   public int keyBitCount() {
      return 4096;
   }

   @Override
   public boolean validateProperty(Property property) {
      Signature signature = this.signature();
      byte[] expected = Base64.getDecoder().decode(property.getSignature());

      try {
         signature.update(property.getValue().getBytes());
         return signature.verify(expected);
      } catch (SignatureException var5) {
         LOGGER.error("Failed to verify signature on property {}", property, var5);
         return false;
      }
   }
}
