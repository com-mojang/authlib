package com.mojang.authlib.minecraft;

import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;

public record BanDetails(UUID id, @Nullable Instant expires, @Nullable String reason) {
   public static final String MULTIPLAYER_SCOPE = "MULTIPLAYER";
}
