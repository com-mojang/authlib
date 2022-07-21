package com.mojang.authlib.yggdrasil;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.HttpAuthenticationService;
import com.mojang.authlib.ProfileLookupCallback;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.response.ProfileSearchResultsResponse;
import java.util.Set;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YggdrasilGameProfileRepository implements GameProfileRepository {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String BASE_URL = "https://api.mojang.com/";
   private static final String SEARCH_PAGE_URL = "https://api.mojang.com/profiles/page/";
   private static final int MAX_FAIL_COUNT = 3;
   private static final int DELAY_BETWEEN_PAGES = 100;
   private static final int DELAY_BETWEEN_FAILURES = 750;
   private final YggdrasilAuthenticationService authenticationService;

   public YggdrasilGameProfileRepository(YggdrasilAuthenticationService authenticationService) {
      this.authenticationService = authenticationService;
   }

   @Override
   public void findProfilesByNames(String[] names, Agent agent, ProfileLookupCallback callback) {
      Set<YggdrasilGameProfileRepository.ProfileCriteria> criteria = Sets.newHashSet();

      for(String name : names) {
         if (!Strings.isNullOrEmpty(name)) {
            criteria.add(new YggdrasilGameProfileRepository.ProfileCriteria(name, agent));
         }
      }

      Exception exception = null;
      Set<YggdrasilGameProfileRepository.ProfileCriteria> request = Sets.newHashSet(criteria);
      int page = 1;
      int failCount = 0;

      while(!criteria.isEmpty()) {
         try {
            ProfileSearchResultsResponse response = this.authenticationService
               .makeRequest(HttpAuthenticationService.constantURL("https://api.mojang.com/profiles/page/" + page), request, ProfileSearchResultsResponse.class);
            failCount = 0;
            exception = null;
            if (response.getSize() == 0 || response.getProfiles().length == 0) {
               LOGGER.debug("Page {} returned empty, aborting search", new Object[]{page});
               break;
            }

            LOGGER.debug("Page {} returned {} results of {}, parsing", new Object[]{page, response.getProfiles().length, response.getSize()});

            for(GameProfile profile : response.getProfiles()) {
               LOGGER.debug("Successfully looked up profile {}", new Object[]{profile});
               criteria.remove(new YggdrasilGameProfileRepository.ProfileCriteria(profile.getName(), agent));
               callback.onProfileLookupSucceeded(profile);
            }

            LOGGER.debug("Page {} successfully parsed", new Object[]{page});
            ++page;

            try {
               Thread.sleep(100L);
            } catch (InterruptedException var15) {
            }
         } catch (AuthenticationException var16) {
            exception = var16;
            if (++failCount == 3) {
               break;
            }

            try {
               Thread.sleep(750L);
            } catch (InterruptedException var14) {
            }
         }
      }

      if (criteria.isEmpty()) {
         LOGGER.debug("Successfully found every profile requested");
      } else {
         LOGGER.debug("{} profiles were missing from search results", new Object[]{criteria.size()});
         if (exception == null) {
            exception = new ProfileNotFoundException("Server did not find the requested profile");
         }

         for(YggdrasilGameProfileRepository.ProfileCriteria profileCriteria : criteria) {
            callback.onProfileLookupFailed(new GameProfile(null, profileCriteria.getName()), exception);
         }
      }

   }

   private class ProfileCriteria {
      private final String name;
      private final String agent;

      private ProfileCriteria(String name, Agent agent) {
         this.name = name;
         this.agent = agent.getName();
      }

      public String getName() {
         return this.name;
      }

      public String getAgent() {
         return this.agent;
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (o != null && this.getClass() == o.getClass()) {
            YggdrasilGameProfileRepository.ProfileCriteria that = (YggdrasilGameProfileRepository.ProfileCriteria)o;
            return this.agent.equals(that.agent) && this.name.toLowerCase().equals(that.name.toLowerCase());
         } else {
            return false;
         }
      }

      public int hashCode() {
         return 31 * this.name.toLowerCase().hashCode() + this.agent.hashCode();
      }

      public String toString() {
         return new ToStringBuilder(this).append("agent", this.agent).append("name", this.name).toString();
      }
   }
}
