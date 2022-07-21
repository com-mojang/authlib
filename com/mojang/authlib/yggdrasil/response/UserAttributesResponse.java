package com.mojang.authlib.yggdrasil.response;

import javax.annotation.Nullable;

public class UserAttributesResponse extends Response {
   @Nullable
   private UserAttributesResponse.Privileges privileges;
   @Nullable
   private UserAttributesResponse.ProfanityFilterPreferences profanityFilterPreferences;

   @Nullable
   public UserAttributesResponse.Privileges getPrivileges() {
      return this.privileges;
   }

   @Nullable
   public UserAttributesResponse.ProfanityFilterPreferences getProfanityFilterPreferences() {
      return this.profanityFilterPreferences;
   }

   public static class Privileges {
      @Nullable
      private UserAttributesResponse.Privileges.Privilege onlineChat;
      @Nullable
      private UserAttributesResponse.Privileges.Privilege multiplayerServer;
      @Nullable
      private UserAttributesResponse.Privileges.Privilege multiplayerRealms;
      @Nullable
      private UserAttributesResponse.Privileges.Privilege telemetry;

      public boolean getOnlineChat() {
         return this.onlineChat != null && this.onlineChat.enabled;
      }

      public boolean getMultiplayerServer() {
         return this.multiplayerServer != null && this.multiplayerServer.enabled;
      }

      public boolean getMultiplayerRealms() {
         return this.multiplayerRealms != null && this.multiplayerRealms.enabled;
      }

      public boolean getTelemetry() {
         return this.telemetry != null && this.telemetry.enabled;
      }

      public class Privilege {
         private boolean enabled;
      }
   }

   public static class ProfanityFilterPreferences {
      private boolean profanityFilterOn;

      public boolean isEnabled() {
         return this.profanityFilterOn;
      }
   }
}
