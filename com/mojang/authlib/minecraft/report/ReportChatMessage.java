package com.mojang.authlib.minecraft.report;

public class ReportChatMessage {
   public ReportChatMessageHeader header;
   public ReportChatMessageBody body;
   public String overriddenMessage;
   public boolean messageReported;

   public ReportChatMessage(ReportChatMessageHeader header, ReportChatMessageBody body, String overriddenMessage, boolean messageReported) {
      this.header = header;
      this.body = body;
      this.overriddenMessage = overriddenMessage;
      this.messageReported = messageReported;
   }
}
