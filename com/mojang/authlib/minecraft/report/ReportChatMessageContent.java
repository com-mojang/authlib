package com.mojang.authlib.minecraft.report;

public class ReportChatMessageContent {
   public String plain;
   public String decorated;

   public ReportChatMessageContent(String plain, String decorated) {
      this.plain = plain;
      this.decorated = decorated;
   }
}
