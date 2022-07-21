package com.mojang.authlib.yggdrasil;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.HttpUserAuthentication;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.yggdrasil.request.AuthenticationRequest;
import com.mojang.authlib.yggdrasil.request.RefreshRequest;
import com.mojang.authlib.yggdrasil.response.AuthenticationResponse;
import com.mojang.authlib.yggdrasil.response.RefreshResponse;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YggdrasilUserAuthentication extends HttpUserAuthentication {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String BASE_URL = "https://authserver.mojang.com/";
   private static final URL ROUTE_AUTHENTICATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/authenticate");
   private static final URL ROUTE_REFRESH = HttpAuthenticationService.constantURL("https://authserver.mojang.com/refresh");
   private static final URL ROUTE_VALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/validate");
   private static final URL ROUTE_INVALIDATE = HttpAuthenticationService.constantURL("https://authserver.mojang.com/invalidate");
   private static final URL ROUTE_SIGNOUT = HttpAuthenticationService.constantURL("https://authserver.mojang.com/signout");
   private static final String STORAGE_KEY_ACCESS_TOKEN = "accessToken";
   private final Agent agent;
   private GameProfile[] profiles;
   private String accessToken;
   private boolean isOnline;

   public YggdrasilUserAuthentication(YggdrasilAuthenticationService authenticationService, Agent agent) {
      super(authenticationService);
      this.agent = agent;
   }

   @Override
   public boolean canLogIn() {
      return !this.canPlayOnline()
         && StringUtils.isNotBlank(this.getUsername())
         && (StringUtils.isNotBlank(this.getPassword()) || StringUtils.isNotBlank(this.getAuthenticatedToken()));
   }

   @Override
   public void logIn() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUsername())) {
         throw new InvalidCredentialsException("Invalid username");
      } else {
         if (StringUtils.isNotBlank(this.getAuthenticatedToken())) {
            this.logInWithToken();
         } else {
            if (!StringUtils.isNotBlank(this.getPassword())) {
               throw new InvalidCredentialsException("Invalid password");
            }

            this.logInWithPassword();
         }

      }
   }

   protected void logInWithPassword() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUsername())) {
         throw new InvalidCredentialsException("Invalid username");
      } else if (StringUtils.isBlank(this.getPassword())) {
         throw new InvalidCredentialsException("Invalid password");
      } else {
         LOGGER.info("Logging in with username & password");
         AuthenticationRequest request = new AuthenticationRequest(this, this.getUsername(), this.getPassword());
         AuthenticationResponse response = this.getAuthenticationService().makeRequest(ROUTE_AUTHENTICATE, request, AuthenticationResponse.class);
         if (!response.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
         } else {
            if (response.getUser() != null && response.getUser().getId() != null) {
               this.setUserid(response.getUser().getId());
            } else {
               this.setUserid(this.getUsername());
            }

            this.isOnline = true;
            this.accessToken = response.getAccessToken();
            this.profiles = response.getAvailableProfiles();
            this.setSelectedProfile(response.getSelectedProfile());
         }
      }
   }

   protected void logInWithToken() throws AuthenticationException {
      if (StringUtils.isBlank(this.getUserID())) {
         if (!StringUtils.isBlank(this.getUsername())) {
            throw new InvalidCredentialsException("Invalid uuid & username");
         }

         this.setUserid(this.getUsername());
      }

      if (StringUtils.isBlank(this.getAuthenticatedToken())) {
         throw new InvalidCredentialsException("Invalid access token");
      } else {
         LOGGER.info("Logging in with access token");
         RefreshRequest request = new RefreshRequest(this);
         RefreshResponse response = this.getAuthenticationService().makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
         if (!response.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
         } else {
            this.isOnline = true;
            this.accessToken = response.getAccessToken();
            this.profiles = response.getAvailableProfiles();
            this.setSelectedProfile(response.getSelectedProfile());
         }
      }
   }

   @Override
   public void logOut() {
      super.logOut();
      this.accessToken = null;
      this.profiles = null;
      this.isOnline = false;
   }

   @Override
   public GameProfile[] getAvailableProfiles() {
      return this.profiles;
   }

   @Override
   public boolean isLoggedIn() {
      return StringUtils.isNotBlank(this.accessToken);
   }

   @Override
   public boolean canPlayOnline() {
      return this.isLoggedIn() && this.getSelectedProfile() != null && this.isOnline;
   }

   @Override
   public void selectGameProfile(GameProfile profile) throws AuthenticationException {
      if (!this.isLoggedIn()) {
         throw new AuthenticationException("Cannot change game profile whilst not logged in");
      } else if (this.getSelectedProfile() != null) {
         throw new AuthenticationException("Cannot change game profile. You must log out and back in.");
      } else if (profile != null && ArrayUtils.contains(this.profiles, profile)) {
         RefreshRequest request = new RefreshRequest(this, profile);
         RefreshResponse response = this.getAuthenticationService().makeRequest(ROUTE_REFRESH, request, RefreshResponse.class);
         if (!response.getClientToken().equals(this.getAuthenticationService().getClientToken())) {
            throw new AuthenticationException("Server requested we change our client token. Don't know how to handle this!");
         } else {
            this.isOnline = true;
            this.accessToken = response.getAccessToken();
            this.setSelectedProfile(response.getSelectedProfile());
         }
      } else {
         throw new IllegalArgumentException("Invalid profile '" + profile + "'");
      }
   }

   @Override
   public void loadFromStorage(Map<String, String> credentials) {
      super.loadFromStorage(credentials);
      this.accessToken = (String)credentials.get("accessToken");
   }

   @Override
   public Map<String, String> saveForStorage() {
      Map<String, String> result = super.saveForStorage();
      if (StringUtils.isNotBlank(this.getAuthenticatedToken())) {
         result.put("accessToken", this.getAuthenticatedToken());
      }

      return result;
   }

   @Deprecated
   public String getSessionToken() {
      return this.isLoggedIn() && this.getSelectedProfile() != null && this.canPlayOnline()
         ? String.format("token:%s:%s", this.getAuthenticatedToken(), this.getSelectedProfile().getId())
         : null;
   }

   @Override
   public String getAuthenticatedToken() {
      return this.accessToken;
   }

   public Agent getAgent() {
      return this.agent;
   }

   @Override
   public String toString() {
      return "YggdrasilAuthenticationService{agent="
         + this.agent
         + ", profiles="
         + Arrays.toString(this.profiles)
         + ", selectedProfile="
         + this.getSelectedProfile()
         + ", username='"
         + this.getUsername()
         + '\''
         + ", isLoggedIn="
         + this.isLoggedIn()
         + ", canPlayOnline="
         + this.canPlayOnline()
         + ", accessToken='"
         + this.accessToken
         + '\''
         + ", clientToken='"
         + this.getAuthenticationService().getClientToken()
         + '\''
         + '}';
   }

   public YggdrasilAuthenticationService getAuthenticationService() {
      return (YggdrasilAuthenticationService)super.getAuthenticationService();
   }
}
