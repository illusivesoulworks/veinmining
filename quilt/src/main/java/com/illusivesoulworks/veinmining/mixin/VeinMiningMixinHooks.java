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

package com.illusivesoulworks.veinmining.mixin;

import com.illusivesoulworks.veinmining.VeinMiningMod;
import com.illusivesoulworks.veinmining.common.veinmining.enchantment.VeinMiningEnchantment;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class VeinMiningMixinHooks {

  public static void removeEnchantment(List<EnchantmentInstance> list, Enchantment enchantment) {

    if (enchantment == VeinMiningMod.ENCHANTMENT) {
      list.remove(list.size() - 1);
    }
  }

  public static void addEnchantment(int level, ItemStack stack, boolean allowTreasure,
                                    List<EnchantmentInstance> returnValue) {
    VeinMiningEnchantment enchantment = VeinMiningMod.ENCHANTMENT;

    if ((!enchantment.isTreasureOnly() || allowTreasure) && enchantment.isDiscoverable() &&
        (enchantment.canApplyAtEnchantingTable(stack) ||
            (stack.is(Items.BOOK) && enchantment.isAllowedOnBooks()))) {

      for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {

        if (level >= enchantment.getMinCost(i) && level <= enchantment.getMaxCost(i)) {
          returnValue.add(new EnchantmentInstance(enchantment, i));
          break;
        }
      }
    }
  }
}
