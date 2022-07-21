package com.mojang.authlib.yggdrasil.response;

import com.google.gson.annotations.SerializedName;
import java.nio.ByteBuffer;
import javax.annotation.Nullable;

public class KeyPairResponse extends Response {
   private KeyPairResponse.KeyPair keyPair;
   @Nullable
   @SerializedName("publicKeySignature")
   private ByteBuffer legacyPublicKeySignature;
   @SerializedName("publicKeySignatureV2")
   private ByteBuffer publicKeySignature;
   private String expiresAt;
   private String refreshedAfter;

   public String getPrivateKey() {
      return this.keyPair.privateKey;
   }

   public String getPublicKey() {
      return this.keyPair.publicKey;
   }

   @Nullable
   public ByteBuffer getLegacyPublicKeySignature() {
      return this.legacyPublicKeySignature;
   }

   public ByteBuffer getPublicKeySignature() {
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
