package com.mojang.authlib.minecraft.report;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ReportChatMessageBody {
   public Instant timestamp;
   public long salt;
   public List<ReportChatMessageBody.LastSeenSignature> lastSeenSignatures;
   public ReportChatMessageContent message;

   public ReportChatMessageBody(
      Instant timestamp, long salt, List<ReportChatMessageBody.LastSeenSignature> lastSeenSignatures, ReportChatMessageContent message
   ) {
      this.timestamp = timestamp;
      this.salt = salt;
      this.lastSeenSignatures = lastSeenSignatures;
      this.message = message;
   }

   public static class LastSeenSignature {
      public UUID profileId;
      public ByteBuffer lastSignature;

      public LastSeenSignature(UUID profileId, ByteBuffer lastSignature) {
         this.profileId = profileId;
         this.lastSignature = lastSignature;
      }
   }
}
