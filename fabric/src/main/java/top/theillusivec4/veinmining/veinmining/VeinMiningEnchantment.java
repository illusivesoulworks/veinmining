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

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class VeinMiningEnchantment extends Enchantment {

  public VeinMiningEnchantment() {
    super(Rarity.RARE, EnchantmentTarget.DIGGER, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
  }

  @Override
  public Rarity getRarity() {
    return VeinMiningConfig.Enchantment.rarity;
  }

  @Override
  public int getMaxLevel() {
    return VeinMiningConfig.Enchantment.levels;
  }

  @Override
  public int getMinPower(int level) {
    return VeinMiningConfig.Enchantment.minPowerBase +
        (level - 1) * VeinMiningConfig.Enchantment.minPowerPerLevel;
  }

  @Override
  public int getMaxPower(int level) {
    return super.getMinPower(level) + 50;
  }

  @Override
  public boolean isTreasure() {
    return VeinMiningConfig.Enchantment.isTreasure;
  }

  @Override
  public boolean isAvailableForEnchantedBookOffer() {
    return VeinMiningConfig.Enchantment.isAvailableOnBooks;
  }

  @Override
  public boolean isAvailableForRandomSelection() {
    return VeinMiningConfig.Enchantment.isRandomlySelectable;
  }

  @Override
  public boolean isAcceptableItem(ItemStack stack) {
    System.out.println("Hiiiii " + stack + " is mining tool?  - " + stack.getItem() + " - ["+(stack.getItem() instanceof MiningToolItem)+"]");
    return stack.getItem() instanceof MiningToolItem;
  }

  @Override
  protected boolean canAccept(Enchantment other) {
    Identifier id = Registry.ENCHANTMENT.getId(other);

    if (id != null &&
        VeinMiningConfig.Enchantment.incompatibleEnchantments.contains(id.toString())) {
      return false;
    }
    return super.canAccept(other);
  }
}
