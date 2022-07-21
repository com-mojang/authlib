package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Environment;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.response.BlockListResponse;
import com.mojang.authlib.yggdrasil.response.PrivilegesResponse;
import java.net.URL;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class YggdrasilSocialInteractionsService implements SocialInteractionsService {
   private final URL routePrivileges;
   private final URL routeBlocklist;
   private final YggdrasilAuthenticationService authenticationService;
   private final String accessToken;
   private boolean serversAllowed;
   private boolean realmsAllowed;
   private boolean chatAllowed;
   @Nullable
   private Set<UUID> blockList;

   public YggdrasilSocialInteractionsService(YggdrasilAuthenticationService authenticationService, String accessToken, Environment env) throws AuthenticationException {
      this.authenticationService = authenticationService;
      this.accessToken = accessToken;
      this.routePrivileges = HttpAuthenticationService.constantURL(env.getServicesHost() + "/privileges");
      this.routeBlocklist = HttpAuthenticationService.constantURL(env.getServicesHost() + "/privacy/blocklist");
      this.checkPrivileges();
   }

   @Override
   public boolean serversAllowed() {
      return this.serversAllowed;
   }

   @Override
   public boolean realmsAllowed() {
      return this.realmsAllowed;
   }

   @Override
   public boolean chatAllowed() {
      return this.chatAllowed;
   }

   @Override
   public boolean isBlockedPlayer(UUID playerID) {
      if (this.blockList == null) {
         this.blockList = this.fetchBlockList();
         if (this.blockList == null) {
            return false;
         }
      }

      return this.blockList.contains(playerID);
   }

   @Nullable
   private Set<UUID> fetchBlockList() {
      try {
         BlockListResponse response = this.authenticationService.makeRequest(this.routeBlocklist, null, BlockListResponse.class, "Bearer " + this.accessToken);
         return response == null ? null : response.getBlockedProfiles();
      } catch (AuthenticationException var2) {
         return null;
      }
   }

   private void checkPrivileges() throws AuthenticationException {
      PrivilegesResponse response = this.authenticationService.makeRequest(this.routePrivileges, null, PrivilegesResponse.class, "Bearer " + this.accessToken);
      if (response == null) {
         throw new AuthenticationUnavailableException();
      } else {
         this.chatAllowed = response.getPrivileges().getOnlineChat().isEnabled();
         this.serversAllowed = response.getPrivileges().getMultiplayerServer().isEnabled();
         this.realmsAllowed = response.getPrivileges().getMultiplayerRealms().isEnabled();
      }
   }
}
