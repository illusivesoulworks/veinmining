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

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class VeinMiningEnchantment extends Enchantment {

  public static final String ID = VeinMiningMod.MOD_ID + ":vein_mining";
  public static final EnchantmentType TYPE =
      EnchantmentType.create(ID, VeinMiningEnchantment::canEnchantItem);
  public static final Map<String, Predicate<ItemStack>> PREDICATE_MAP;

  static {
    Map<String, Predicate<ItemStack>> temp = new HashMap<>();
    temp.put("is:tool", stack -> !stack.getToolTypes().isEmpty());
    temp.put("is:pickaxe", stack -> isToolType(ToolType.PICKAXE, stack));
    temp.put("is:axe", stack -> isToolType(ToolType.AXE, stack));
    temp.put("is:hoe", stack -> isToolType(ToolType.HOE, stack));
    temp.put("is:shovel", stack -> isToolType(ToolType.SHOVEL, stack));
    PREDICATE_MAP = ImmutableMap.copyOf(temp);
  }

  public VeinMiningEnchantment() {
    super(Rarity.RARE, TYPE, new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
  }

  private static boolean isToolType(ToolType type, ItemStack stack) {
    return stack.getToolTypes().contains(type);
  }

  private static boolean canEnchantItem(Item item) {

    for (String entry : VeinMiningConfig.Enchantment.items) {

      if (PREDICATE_MAP.getOrDefault(entry, k -> false).test(new ItemStack(item))) {
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
  protected boolean canApplyTogether(Enchantment ench) {
    ResourceLocation rl = ench.getRegistryName();

    if (rl != null &&
        VeinMiningConfig.Enchantment.incompatibleEnchantments.contains(rl.toString())) {
      return false;
    }
    return super.canApplyTogether(ench);
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
