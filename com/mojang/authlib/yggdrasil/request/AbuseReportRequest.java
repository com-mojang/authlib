package com.mojang.authlib.yggdrasil.request;

import com.mojang.authlib.minecraft.report.AbuseReport;
import java.util.UUID;

public class AbuseReportRequest {
   public UUID id;
   public AbuseReport report;
   public AbuseReportRequest.ClientInfo clientInfo;
   public AbuseReportRequest.ThirdPartyServerInfo thirdPartyServerInfo;
   public AbuseReportRequest.RealmInfo realmInfo;

   public AbuseReportRequest(
      UUID id,
      AbuseReport report,
      AbuseReportRequest.ClientInfo clientInfo,
      AbuseReportRequest.ThirdPartyServerInfo thirdPartyServerInfo,
      AbuseReportRequest.RealmInfo realmInfo
   ) {
      this.id = id;
      this.report = report;
      this.clientInfo = clientInfo;
      this.thirdPartyServerInfo = thirdPartyServerInfo;
      this.realmInfo = realmInfo;
   }

   public static class ClientInfo {
      public String clientVersion;

      public ClientInfo(String clientVersion) {
         this.clientVersion = clientVersion;
      }
   }

   public static class RealmInfo {
      public String realmId;
      public int slotId;

      public RealmInfo(String realmId, int slotId) {
         this.realmId = realmId;
         this.slotId = slotId;
      }
   }

   public static class ThirdPartyServerInfo {
      public String address;

      public ThirdPartyServerInfo(String address) {
         this.address = address;
      }
   }
}
