package com.mojang.authlib.minecraft;

import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
import com.mojang.authlib.yggdrasil.response.KeyPairResponse;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;

public interface UserApiService {
   UserApiService.UserProperties OFFLINE_PROPERTIES = new UserApiService.UserProperties(
      Set.of(UserApiService.UserFlag.CHAT_ALLOWED, UserApiService.UserFlag.REALMS_ALLOWED, UserApiService.UserFlag.SERVERS_ALLOWED)
   );
   UserApiService OFFLINE = new UserApiService() {
      @Override
      public UserApiService.UserProperties properties() {
         return OFFLINE_PROPERTIES;
      }

      @Override
      public boolean isBlockedPlayer(UUID playerID) {
         return false;
      }

      @Override
      public void refreshBlockList() {
      }

      @Override
      public TelemetrySession newTelemetrySession(Executor executor) {
         return TelemetrySession.DISABLED;
      }

      @Nullable
      @Override
      public KeyPairResponse getKeyPair() {
         return null;
      }

      @Override
      public void reportAbuse(AbuseReportRequest request) {
      }

      @Override
      public boolean canSendReports() {
         return false;
      }

      @Override
      public AbuseReportLimits getAbuseReportLimits() {
         return AbuseReportLimits.DEFAULTS;
      }
   };

   UserApiService.UserProperties properties();

   boolean isBlockedPlayer(UUID var1);

   void refreshBlockList();

   TelemetrySession newTelemetrySession(Executor var1);

   @Nullable
   KeyPairResponse getKeyPair();

   void reportAbuse(AbuseReportRequest var1);

   boolean canSendReports();

   AbuseReportLimits getAbuseReportLimits();

   public static enum UserFlag {
      SERVERS_ALLOWED,
      REALMS_ALLOWED,
      CHAT_ALLOWED,
      TELEMETRY_ENABLED,
      PROFANITY_FILTER_ENABLED;
   }

   public static record UserProperties(Set<UserApiService.UserFlag> flags) {
      public boolean flag(UserApiService.UserFlag flag) {
         return this.flags.contains(flag);
      }
   }
}
