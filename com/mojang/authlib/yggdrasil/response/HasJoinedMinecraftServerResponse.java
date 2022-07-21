package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.ProfileProperty;
import java.util.List;

public class HasJoinedMinecraftServerResponse extends Response {
   private String id;
   private List<ProfileProperty> properties;

   public String getId() {
      return this.id;
   }

   public List<ProfileProperty> getProperties() {
      return this.properties;
   }
}
