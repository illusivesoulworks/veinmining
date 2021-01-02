/*
 * Copyright (c) 2020 C4
 *
 * This file is part of Vein Mining, a mod made for Minecraft.
 *
 * Vein Mining is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Vein Mining.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.veinmining.veinmining;

import javax.annotation.Nonnull;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class VeinMiningEnchantment extends Enchantment {

  private static final String ID = VeinMiningMod.MOD_ID + ":vein_mining";

  public VeinMiningEnchantment() {
    super(Rarity.RARE, EnchantmentType.DIGGER,
        new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    this.setRegistryName(ID);
  }

  @Nonnull
  @Override
  public Rarity getRarity() {
    return VeinMiningConfig.Enchantment.rarity;
  }

  @Override
  public int getMaxLevel() {
    return VeinMiningConfig.Enchantment.levels;
  }

  @Override
  public int getMinEnchantability(int enchantmentLevel) {
    return VeinMiningConfig.Enchantment.minEnchantabilityBase +
        VeinMiningConfig.Enchantment.minEnchantabilityPerLevel * (enchantmentLevel - 1);
  }

  @Override
  public int getMaxEnchantability(int enchantmentLevel) {
    return this.getMinEnchantability(enchantmentLevel) + 50;
  }

  @Override
  public boolean isTreasureEnchantment() {
    return VeinMiningConfig.Enchantment.isTreasure;
  }

  @Override
  public boolean canVillagerTrade() {
    return VeinMiningConfig.Enchantment.isVillagerTrade;
  }

  @Override
  public boolean canGenerateInLoot() {
    return VeinMiningConfig.Enchantment.isLootable;
  }

  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    return super.canApplyAtEnchantingTable(stack) && VeinMiningConfig.Enchantment.canApplyAtEnchantingTable;
  }

  @Override
  public boolean isAllowedOnBooks() {
    return VeinMiningConfig.Enchantment.canApplyOnBooks;
  }
}
