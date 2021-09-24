/*
 * Copyright (C) 2020-2021 C4
 *
 * This file is part of Vein Mining.
 *
 * Vein Mining is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Vein Mining.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package top.theillusivec4.veinmining.veinmining;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class VeinMiningEnchantment extends Enchantment {

  public static final String ID = VeinMiningMod.MOD_ID + ":vein_mining";

  public static final EnchantmentCategory TYPE =
      EnchantmentCategory.create(ID, VeinMiningEnchantment::canEnchantItem);
  public static final Map<String, Predicate<Item>> PREDICATE_MAP;

  static {
    Map<String, Predicate<Item>> temp = new HashMap<>();
    temp.put("is:tool", item -> item instanceof DiggerItem);
    temp.put("is:pickaxe", item -> item instanceof PickaxeItem);
    temp.put("is:axe", item -> item instanceof AxeItem);
    temp.put("is:hoe", item -> item instanceof HoeItem);
    temp.put("is:shovel", item -> item instanceof ShovelItem);
    PREDICATE_MAP = ImmutableMap.copyOf(temp);
  }

  public VeinMiningEnchantment() {
    super(Rarity.RARE, TYPE, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
  }

  private static boolean canEnchantItem(Item item) {

    for (String entry : VeinMiningConfig.Enchantment.items) {

      if (PREDICATE_MAP.getOrDefault(entry, k -> false).test(item)) {
        return true;
      } else if (item.getRegistryName() != null &&
          item.getRegistryName().toString().equals(entry)) {
        return true;
      }
    }
    return false;
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
  public int getMinCost(int enchantmentLevel) {
    return VeinMiningConfig.Enchantment.minEnchantabilityBase +
        VeinMiningConfig.Enchantment.minEnchantabilityPerLevel * (enchantmentLevel - 1);
  }

  @Override
  public int getMaxCost(int enchantmentLevel) {
    return this.getMinCost(enchantmentLevel) + 50;
  }

  @Override
  public boolean isTreasureOnly() {
    return VeinMiningConfig.Enchantment.isTreasure;
  }

  @Override
  public boolean isTradeable() {
    return VeinMiningConfig.Enchantment.isVillagerTrade;
  }

  @Override
  public boolean isDiscoverable() {
    return VeinMiningConfig.Enchantment.isLootable;
  }

  @Override
  protected boolean checkCompatibility(Enchantment ench) {
    ResourceLocation rl = ench.getRegistryName();

    if (rl != null &&
        VeinMiningConfig.Enchantment.incompatibleEnchantments.contains(rl.toString())) {
      return false;
    }
    return super.checkCompatibility(ench);
  }

  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    return super.canApplyAtEnchantingTable(stack) &&
        VeinMiningConfig.Enchantment.canApplyAtEnchantingTable;
  }

  @Override
  public boolean isAllowedOnBooks() {
    return VeinMiningConfig.Enchantment.canApplyOnBooks;
  }
}
