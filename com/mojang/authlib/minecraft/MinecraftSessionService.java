package com.mojang.authlib.minecraft;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;

public interface MinecraftSessionService {
   void joinServer(GameProfile var1, String var2, String var3) throws AuthenticationException;

   GameProfile hasJoinedServer(GameProfile var1, String var2) throws AuthenticationUnavailableException;
}
