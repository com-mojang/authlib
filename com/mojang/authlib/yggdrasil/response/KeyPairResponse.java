package com.mojang.authlib.yggdrasil.response;

import com.google.gson.annotations.SerializedName;
import javax.annotation.Nullable;

public class KeyPairResponse extends Response {
   private KeyPairResponse.KeyPair keyPair;
   @Nullable
   @SerializedName("publicKeySignature")
   private String legacyPublicKeySignature;
   @SerializedName("publicKeySignatureV2")
   private String publicKeySignature;
   private String expiresAt;
   private String refreshedAfter;

   public String getPrivateKey() {
      return this.keyPair.privateKey;
   }

   public String getPublicKey() {
      return this.keyPair.publicKey;
   }

   @Nullable
   public String getLegacyPublicKeySignature() {
      return this.legacyPublicKeySignature;
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
