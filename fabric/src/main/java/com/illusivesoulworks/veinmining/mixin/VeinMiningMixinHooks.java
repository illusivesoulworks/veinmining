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

import com.illusivesoulworks.veinmining.common.veinmining.enchantment.VeinMiningEnchantment;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class VeinMiningMixinHooks {

  public static List<EnchantmentInstance> filterEnchantments(
      List<EnchantmentInstance> list, int level, ItemStack stack, boolean allowTreasure) {
    List<EnchantmentInstance> result = new ArrayList<>();

    for (EnchantmentInstance enchantmentInstance : list) {
      Enchantment enchantment = enchantmentInstance.enchantment;

      if (enchantment instanceof VeinMiningEnchantment veinMiningEnchantment) {

        if ((!enchantment.isTreasureOnly() || allowTreasure) && enchantment.isDiscoverable() &&
            (veinMiningEnchantment.canApplyAtEnchantingTable(stack) ||
                (stack.is(Items.BOOK) && veinMiningEnchantment.isAllowedOnBooks()))) {

          for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {

            if (level >= enchantment.getMinCost(i) && level <= enchantment.getMaxCost(i)) {
              result.add(new EnchantmentInstance(enchantment, i));
              break;
            }
          }
        }
      } else {
        result.add(enchantmentInstance);
      }
    }
    return result;
  }
}
