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
import java.util.Set;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class VeinMiningEnchantment extends Enchantment {

  public static final String ID = VeinMiningMod.MOD_ID + ":vein_mining";
  public static final Map<String, Predicate<ItemStack>> PREDICATE_MAP;
  public static final EnchantmentCategory CATEGORY =
      EnchantmentCategory.create(ID, VeinMiningEnchantment::canEnchantItem);

  static {
    Map<String, Predicate<ItemStack>> temp = new HashMap<>();
    temp.put("is:tool", VeinMiningEnchantment::canToolAction);
    temp.put("is:pickaxe", stack -> canToolAction(ToolActions.PICKAXE_DIG, stack));
    temp.put("is:axe", stack -> canToolAction(ToolActions.AXE_DIG, stack));
    temp.put("is:hoe", stack -> canToolAction(ToolActions.HOE_DIG, stack));
    temp.put("is:shovel", stack -> canToolAction(ToolActions.SHOVEL_DIG, stack));
    PREDICATE_MAP = ImmutableMap.copyOf(temp);
  }

  public VeinMiningEnchantment() {
    super(Rarity.RARE, CATEGORY, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
  }

  private static boolean canToolAction(ItemStack stack) {
    Set<ToolAction> actions =
        Set.of(ToolActions.PICKAXE_DIG, ToolActions.AXE_DIG, ToolActions.HOE_DIG,
            ToolActions.SHOVEL_DIG);

    for (ToolAction action : actions) {

      if (stack.canPerformAction(action)) {
        return true;
      }
    }
    return false;
  }

  private static boolean canToolAction(ToolAction toolAction, ItemStack stack) {
    return stack.canPerformAction(toolAction);
  }

  private static boolean canEnchantItem(Item item) {

    for (String entry : VeinMiningConfig.Enchantment.items) {

      if (item.toString().equals(entry)) {
        return true;
      }
    }
    return item instanceof DiggerItem;
  }

  private static boolean canEnchantItem(ItemStack stack) {
    for (String entry : VeinMiningConfig.Enchantment.items) {
      if (PREDICATE_MAP.getOrDefault(entry, k -> false).test(stack)) {
        return true;
      } else{
        if (ForgeRegistries.ITEMS.getKey(stack.getItem()) != null &&
          ForgeRegistries.ITEMS.getKey(stack.getItem()).toString().equals(entry)) {
          return true;
        }
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
    ResourceLocation rl = ForgeRegistries.ENCHANTMENTS.getKey(ench);

    if (rl != null &&
        VeinMiningConfig.Enchantment.incompatibleEnchantments.contains(rl.toString())) {
      return false;
    }

    return super.checkCompatibility(ench);
  }

  @Override
  public boolean canEnchant(@Nonnull ItemStack pStack) {
    return canEnchantItem(pStack);
  }

  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack pStack) {
    return this.canEnchant(pStack) && VeinMiningConfig.Enchantment.canApplyAtEnchantingTable;
  }

  @Override
  public boolean isAllowedOnBooks() {
    return VeinMiningConfig.Enchantment.canApplyOnBooks;
  }
}
