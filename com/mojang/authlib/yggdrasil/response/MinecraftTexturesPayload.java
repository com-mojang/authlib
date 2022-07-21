package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;

public class MinecraftTexturesPayload {
   private long timestamp;
   private String profileId;
   private String profileName;
   private Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures;

   public long getTimestamp() {
      return this.timestamp;
   }

   public String getProfileId() {
      return this.profileId;
   }

   public String getProfileName() {
      return this.profileName;
   }

   public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures() {
      return this.textures;
   }
}
