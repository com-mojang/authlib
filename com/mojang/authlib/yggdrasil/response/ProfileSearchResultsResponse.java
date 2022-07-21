package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.GameProfile;

public class ProfileSearchResultsResponse extends Response {
   private GameProfile[] profiles;
   private int size;

   public GameProfile[] getProfiles() {
      return this.profiles;
   }

   public int getSize() {
      return this.size;
   }
}
