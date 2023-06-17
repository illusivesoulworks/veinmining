/*
 * Copyright (C) 2020-2022 Illusive Soulworks
 *
 * Vein Mining is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Vein Mining.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.veinmining.common.veinmining.enchantment;

import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.platform.Services;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemProcessor {

  public static final Set<ItemChecker> ITEM_CHECKERS = new HashSet<>();

  public static synchronized void rebuild() {
    ITEM_CHECKERS.clear();
    VeinMiningConfig.COMMON.itemsList.clearCache();

    for (String s : VeinMiningConfig.COMMON.itemsList.getTransformed()) {

      if (s.startsWith("#")) {
        ResourceLocation rl = ResourceLocation.tryParse(s.substring(1));
        Set<String> items = Services.PLATFORM.getItemsFromTag(rl);

        for (String item : items) {
          ResourceLocation irl = ResourceLocation.tryParse(item);

          if (irl != null) {
            ITEM_CHECKERS.add(new ItemChecker(irl));
          }
        }
      } else {
        ResourceLocation rl = ResourceLocation.tryParse(s);

        if (rl != null) {
          ITEM_CHECKERS.add(new ItemChecker(rl));
        }
      }
    }
  }

  public record ItemChecker(ResourceLocation resourceLocation) {

    private static final Map<String, Predicate<ItemStack>> PREDICATE_MAP =
        Services.PLATFORM.buildEnchantableItems();
    private static final BiPredicate<ResourceLocation, ItemStack> DEFAULT_PREDICATE =
        (resourceLocation, stack) -> Services.PLATFORM.getItem(resourceLocation)
            .map(item -> item == stack.getItem()).orElse(false);

    public boolean test(ItemStack stack) {
      Predicate<ItemStack> predicate = PREDICATE_MAP.get(this.resourceLocation.toString());
      return predicate != null ? predicate.test(stack) :
          DEFAULT_PREDICATE.test(this.resourceLocation, stack);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ItemChecker that = (ItemChecker) o;
      return resourceLocation.equals(that.resourceLocation);
    }

    @Override
    public int hashCode() {
      return Objects.hash(resourceLocation);
    }
  }
}
