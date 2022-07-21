package com.mojang.authlib.minecraft.report;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ReportChatMessageHeader {
   public ByteBuffer signatureOfPreviousHeader;
   public UUID profileId;
   public ByteBuffer hashOfBody;
   public ByteBuffer signature;

   public ReportChatMessageHeader(ByteBuffer signatureOfPreviousHeader, UUID profileId, ByteBuffer hashOfBody, ByteBuffer signature) {
      this.signatureOfPreviousHeader = signatureOfPreviousHeader;
      this.profileId = profileId;
      this.hashOfBody = hashOfBody;
      this.signature = signature;
   }
}
