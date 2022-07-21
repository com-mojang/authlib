package com.mojang.authlib.yggdrasil.response;

import com.mojang.authlib.GameProfile;

public class AuthenticationResponse extends Response {
   private String accessToken;
   private String clientToken;
   private GameProfile selectedProfile;
   private GameProfile[] availableProfiles;
   private AuthenticationResponse.User user;

   public String getAccessToken() {
      return this.accessToken;
   }

   public String getClientToken() {
      return this.clientToken;
   }

   public GameProfile[] getAvailableProfiles() {
      return this.availableProfiles;
   }

   public GameProfile getSelectedProfile() {
      return this.selectedProfile;
   }

   public AuthenticationResponse.User getUser() {
      return this.user;
   }

   public class User {
      private String id;

      public String getId() {
         return this.id;
      }
   }
}
