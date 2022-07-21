package com.mojang.authlib;

import com.mojang.authlib.properties.PropertyMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class GameProfile {
   private final String id;
   private final String name;
   private final PropertyMap properties = new PropertyMap();
   private boolean legacy;

   public GameProfile(String id, String name) {
      if (StringUtils.isBlank(id) && StringUtils.isBlank(name)) {
         throw new IllegalArgumentException("Name and ID cannot both be blank");
      } else {
         this.id = id;
         this.name = name;
      }
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public PropertyMap getProperties() {
      return this.properties;
   }

   public boolean isComplete() {
      return StringUtils.isNotBlank(this.getId()) && StringUtils.isNotBlank(this.getName());
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         GameProfile that = (GameProfile)o;
         if (!this.id.equals(that.id)) {
            return false;
         } else {
            return this.name.equals(that.name);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.id.hashCode();
      return 31 * result + this.name.hashCode();
   }

   public String toString() {
      return new ToStringBuilder(this)
         .append("id", this.id)
         .append("name", this.name)
         .append("properties", this.properties)
         .append("legacy", this.legacy)
         .toString();
   }

   public boolean isLegacy() {
      return this.legacy;
   }
}
