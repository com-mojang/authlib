package com.mojang.authlib.yggdrasil.response;

public class KeyPairResponse extends Response {
   private KeyPairResponse.KeyPair keyPair;
   private String publicKeySignature;
   private String expiresAt;
   private String refreshedAfter;

   public String getPrivateKey() {
      return this.keyPair.privateKey;
   }

   public String getPublicKey() {
      return this.keyPair.publicKey;
   }

   public String getPublicKeySignature() {
      return this.publicKeySignature;
   }

   public String getExpiresAt() {
      return this.expiresAt;
   }

   public String getRefreshedAfter() {
      return this.refreshedAfter;
   }

   private static final class KeyPair {
      private String privateKey;
      private String publicKey;
   }
}
