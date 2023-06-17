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
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class VeinMiningEnchantment extends Enchantment {

  public VeinMiningEnchantment() {
    super(Rarity.RARE, Services.PLATFORM.getEnchantmentCategory(),
        new EquipmentSlot[] {EquipmentSlot.MAINHAND});
  }

  public static boolean canEnchant(Item item) {

    for (String entry : VeinMiningConfig.COMMON.itemsList.getTransformed()) {
      ResourceLocation resourceLocation = ResourceLocation.tryParse(entry);

      if (resourceLocation != null &&
          Services.PLATFORM.getItem(resourceLocation).map(found -> found == item).orElse(false)) {
        return true;
      }
    }
    return item instanceof DiggerItem;
  }

  private static boolean canEnchantStack(ItemStack stack) {

    for (ItemProcessor.ItemChecker entry : ItemProcessor.ITEM_CHECKERS) {

      if (entry.test(stack)) {
        return true;
      }
    }
    return false;
  }

  @Nonnull
  @Override
  public Rarity getRarity() {
    return VeinMiningConfig.COMMON.rarity.get();
  }

  @Override
  public int getMaxLevel() {
    return VeinMiningConfig.COMMON.levels.get();
  }

  @Override
  public int getMinCost(int enchantmentLevel) {
    return VeinMiningConfig.COMMON.minEnchantabilityBase.get() +
        VeinMiningConfig.COMMON.minEnchantabilityPerLevel.get() * (enchantmentLevel - 1);
  }

  @Override
  public int getMaxCost(int enchantmentLevel) {
    return this.getMinCost(enchantmentLevel) + 50;
  }

  @Override
  public boolean isTreasureOnly() {
    return VeinMiningConfig.COMMON.isTreasure.get();
  }

  @Override
  public boolean isTradeable() {
    return VeinMiningConfig.COMMON.isVillagerTrade.get();
  }

  @Override
  public boolean isDiscoverable() {
    return VeinMiningConfig.COMMON.isLootable.get();
  }

  @Override
  protected boolean checkCompatibility(@Nonnull Enchantment ench) {
    return !VeinMiningConfig.COMMON.incompatibleEnchantments.getTransformed().contains(ench) &&
        super.checkCompatibility(ench);
  }

  @Override
  public boolean canEnchant(@Nonnull ItemStack stack) {
    return canEnchantStack(stack);
  }

  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    return this.canEnchant(stack) && VeinMiningConfig.COMMON.canApplyAtEnchantingTable.get();
  }

  public boolean isAllowedOnBooks() {
    return VeinMiningConfig.COMMON.canApplyOnBooks.get();
  }
}
