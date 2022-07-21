package com.mojang.authlib;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public abstract class BaseUserAuthentication implements UserAuthentication {
   protected static final String STORAGE_KEY_PROFILE_NAME = "displayName";
   protected static final String STORAGE_KEY_PROFILE_ID = "uuid";
   protected static final String STORAGE_KEY_USER_NAME = "username";
   protected static final String STORAGE_KEY_USER_ID = "userid";
   private final AuthenticationService authenticationService;
   private String userid;
   private String username;
   private String password;
   private GameProfile selectedProfile;

   protected BaseUserAuthentication(AuthenticationService authenticationService) {
      Validate.notNull(authenticationService);
      this.authenticationService = authenticationService;
   }

   @Override
   public boolean canLogIn() {
      return !this.canPlayOnline() && StringUtils.isNotBlank(this.getUsername()) && StringUtils.isNotBlank(this.getPassword());
   }

   @Override
   public void logOut() {
      this.password = null;
      this.userid = null;
      this.setSelectedProfile(null);
   }

   @Override
   public boolean isLoggedIn() {
      return this.getSelectedProfile() != null;
   }

   @Override
   public void setUsername(String username) {
      if (this.isLoggedIn() && this.canPlayOnline()) {
         throw new IllegalStateException("Cannot change username whilst logged in & online");
      } else {
         this.username = username;
      }
   }

   @Override
   public void setPassword(String password) {
      if (this.isLoggedIn() && this.canPlayOnline() && StringUtils.isNotBlank(password)) {
         throw new IllegalStateException("Cannot set password whilst logged in & online");
      } else {
         this.password = password;
      }
   }

   protected String getUsername() {
      return this.username;
   }

   protected String getPassword() {
      return this.password;
   }

   @Override
   public void loadFromStorage(Map<String, String> credentials) {
      this.logOut();
      this.setUsername((String)credentials.get("username"));
      if (credentials.containsKey("userid")) {
         this.userid = (String)credentials.get("userid");
      } else {
         this.userid = this.username;
      }

      if (credentials.containsKey("displayName") && credentials.containsKey("uuid")) {
         this.setSelectedProfile(new GameProfile((String)credentials.get("uuid"), (String)credentials.get("displayName")));
      }

   }

   @Override
   public Map<String, String> saveForStorage() {
      Map<String, String> result = new HashMap();
      if (this.getUsername() != null) {
         result.put("username", this.getUsername());
      }

      if (this.getUserID() != null) {
         result.put("userid", this.getUserID());
      } else if (this.getUsername() != null) {
         result.put("username", this.getUsername());
      }

      if (this.getSelectedProfile() != null) {
         result.put("displayName", this.getSelectedProfile().getName());
         result.put("uuid", this.getSelectedProfile().getId());
      }

      return result;
   }

   protected void setSelectedProfile(GameProfile selectedProfile) {
      this.selectedProfile = selectedProfile;
   }

   @Override
   public GameProfile getSelectedProfile() {
      return this.selectedProfile;
   }

   public String toString() {
      StringBuilder result = new StringBuilder();
      result.append(this.getClass().getSimpleName());
      result.append("{");
      if (this.isLoggedIn()) {
         result.append("Logged in as ");
         result.append(this.getUsername());
         if (this.getSelectedProfile() != null) {
            result.append(" / ");
            result.append(this.getSelectedProfile());
            result.append(" - ");
            if (this.canPlayOnline()) {
               result.append("Online");
            } else {
               result.append("Offline");
            }
         }
      } else {
         result.append("Not logged in");
      }

      result.append("}");
      return result.toString();
   }

   public AuthenticationService getAuthenticationService() {
      return this.authenticationService;
   }

   @Override
   public String getUserID() {
      return this.userid;
   }

   protected void setUserid(String userid) {
      this.userid = userid;
   }
}
