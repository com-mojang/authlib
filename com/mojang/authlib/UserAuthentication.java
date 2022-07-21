package com.mojang.authlib;

import com.google.common.collect.Multimap;
import com.mojang.authlib.exceptions.AuthenticationException;
import java.util.Map;

public interface UserAuthentication {
   boolean canLogIn();

   void logIn() throws AuthenticationException;

   void logOut();

   boolean isLoggedIn();

   boolean canPlayOnline();

   GameProfile[] getAvailableProfiles();

   GameProfile getSelectedProfile();

   void selectGameProfile(GameProfile var1) throws AuthenticationException;

   void loadFromStorage(Map<String, String> var1);

   Map<String, String> saveForStorage();

   void setUsername(String var1);

   void setPassword(String var1);

   String getAuthenticatedToken();

   String getUserID();

   Multimap<String, String> getUserProperties();
}
