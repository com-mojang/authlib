package com.mojang.authlib.minecraft.report;

import java.time.Instant;
import java.util.UUID;

public class ReportChatMessage {
   public UUID profileId;
   public Instant timestamp;
   public long salt;
   public String signature;
   public String message;
   public String overriddenMessage;
   public boolean messageReported;

   public ReportChatMessage(UUID profileId, Instant timestamp, long salt, String signature, String message, String overriddenMessage, boolean messageReported) {
      this.profileId = profileId;
      this.timestamp = timestamp;
      this.salt = salt;
      this.signature = signature;
      this.message = message;
      this.overriddenMessage = overriddenMessage;
      this.messageReported = messageReported;
   }
}
