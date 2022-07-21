package com.mojang.authlib.minecraft;

import java.util.UUID;
import java.util.concurrent.Executor;

public interface UserApiService {
   UserApiService OFFLINE = new UserApiService() {
      @Override
      public boolean serversAllowed() {
         return true;
      }

      @Override
      public boolean realmsAllowed() {
         return true;
      }

      @Override
      public boolean chatAllowed() {
         return true;
      }

      @Override
      public boolean telemetryAllowed() {
         return false;
      }

      @Override
      public boolean isBlockedPlayer(UUID playerID) {
         return false;
      }

      @Override
      public TelemetrySession newTelemetrySession(Executor executor) {
         return TelemetrySession.DISABLED;
      }
   };

   boolean serversAllowed();

   boolean realmsAllowed();

   boolean chatAllowed();

   boolean telemetryAllowed();

   boolean isBlockedPlayer(UUID var1);

   TelemetrySession newTelemetrySession(Executor var1);
}
