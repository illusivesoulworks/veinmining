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

package top.theillusivec4.veinmining.config;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.veinmining.VeinMiningEnchantment;

public class VeinMiningConfig {

  public static final ForgeConfigSpec CONFIG_SPEC;
  public static final Config CONFIG;
  private static final String CONFIG_PREFIX = "gui." + VeinMiningMod.MOD_ID + ".config.";

  static {
    final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
        .configure(Config::new);
    CONFIG_SPEC = specPair.getRight();
    CONFIG = specPair.getLeft();
  }

  public static void bake() {
    Enchantment.bake();
    VeinMining.bake();
  }

  public static class Enchantment {
    public static net.minecraft.world.item.enchantment.Enchantment.Rarity rarity =
        net.minecraft.world.item.enchantment.Enchantment.Rarity.RARE;
    public static int levels = 1;
    public static boolean isTreasure = false;
    public static boolean isVillagerTrade = true;
    public static boolean isLootable = true;
    public static boolean canApplyAtEnchantingTable = true;
    public static boolean canApplyOnBooks = true;
    public static int minEnchantabilityBase = 15;
    public static int minEnchantabilityPerLevel = 5;
    public static Set<String> incompatibleEnchantments = new HashSet<>();
    public static Set<String> items = new HashSet<>();

    public static void bake() {
      rarity = CONFIG.rarity.get();
      levels = CONFIG.levels.get();
      isTreasure = CONFIG.isTreasure.get();
      isVillagerTrade = CONFIG.isVillagerTrade.get();
      isLootable = CONFIG.isLootable.get();
      canApplyAtEnchantingTable = CONFIG.canApplyAtEnchantingTable.get();
      canApplyOnBooks = CONFIG.canApplyOnBooks.get();
      minEnchantabilityBase = CONFIG.minEnchantabilityBase.get();
      minEnchantabilityPerLevel = CONFIG.minEnchantabilityPerLevel.get();
      incompatibleEnchantments.clear();

      for (String enchantment : CONFIG.incompatibleEnchantments.get()) {

        if (ForgeRegistries.ENCHANTMENTS.containsKey(new ResourceLocation(enchantment))) {
          incompatibleEnchantments.add(enchantment);
        }
      }
      items.clear();

      for (String item : CONFIG.items.get()) {

        if (VeinMiningEnchantment.PREDICATE_MAP.containsKey(item) ||
            ForgeRegistries.ITEMS.containsKey(new ResourceLocation(item))) {
          items.add(item);
        }
      }
    }
  }

  public static class VeinMining {

    public static boolean requireEffectiveTool;
    public static int maxBlocksBase;
    public static int maxDistanceBase;
    public static int maxBlocksPerLevel;
    public static int maxDistancePerLevel;
    public static boolean diagonalMining;
    public static boolean relocateDrops;
    public static boolean preventToolDestruction;
    public static boolean addToolDamage;
    public static int toolDamageMultiplier;
    public static boolean addPlayerExhaustion;
    public static double playerExhaustionMultiplier;
    public static boolean limitedByDurability;
    public static Set<String> blocks = new HashSet<>();
    public static PermissionType blocksPermission = PermissionType.BLACKLIST;
    public static Set<String> groups = new HashSet<>();

    public static void bake() {
      requireEffectiveTool = CONFIG.requireEffectiveTool.get();
      maxBlocksBase = CONFIG.maxBlocksBase.get();
      maxDistanceBase = CONFIG.maxDistanceBase.get();
      maxBlocksPerLevel = CONFIG.maxBlocksPerLevel.get();
      maxDistancePerLevel = CONFIG.maxDistancePerLevel.get();
      diagonalMining = CONFIG.diagonalMining.get();
      relocateDrops = CONFIG.relocateDrops.get();
      preventToolDestruction = CONFIG.preventToolDestruction.get();
      addToolDamage = CONFIG.addToolDamage.get();
      toolDamageMultiplier = CONFIG.toolDamageMultiplier.get();
      addPlayerExhaustion = CONFIG.addPlayerExhaustion.get();
      playerExhaustionMultiplier = CONFIG.playerExhaustionMultiplier.get();
      limitedByDurability = CONFIG.limitedByDurability.get();
      blocks = new HashSet<>();
      blocks.addAll(CONFIG.blocks.get());
      blocksPermission = CONFIG.blocksPermission.get();
      groups = new HashSet<>();
      groups.addAll(CONFIG.groups.get());
    }
  }

  public static class Config {

    public final EnumValue<net.minecraft.world.item.enchantment.Enchantment.Rarity> rarity;
    public final IntValue levels;
    public final BooleanValue isTreasure;
    public final BooleanValue isVillagerTrade;
    public final BooleanValue isLootable;
    public final BooleanValue canApplyAtEnchantingTable;
    public final BooleanValue canApplyOnBooks;
    public final IntValue minEnchantabilityBase;
    public final IntValue minEnchantabilityPerLevel;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> incompatibleEnchantments;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> items;

    public final BooleanValue requireEffectiveTool;
    public final IntValue maxBlocksBase;
    public final IntValue maxDistanceBase;
    public final IntValue maxBlocksPerLevel;
    public final IntValue maxDistancePerLevel;
    public final BooleanValue diagonalMining;
    public final BooleanValue relocateDrops;
    public final BooleanValue preventToolDestruction;
    public final BooleanValue addToolDamage;
    public final IntValue toolDamageMultiplier;
    public final BooleanValue addPlayerExhaustion;
    public final DoubleValue playerExhaustionMultiplier;
    public final BooleanValue limitedByDurability;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> blocks;
    public final EnumValue<PermissionType> blocksPermission;

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> groups;

    public Config(ForgeConfigSpec.Builder builder) {
      builder.push("enchantment");

      rarity = builder.comment("The rarity of the enchantment")
          .translation(CONFIG_PREFIX + "rarity")
          .defineEnum("rarity", net.minecraft.world.item.enchantment.Enchantment.Rarity.RARE);

      levels = builder.comment("The number of levels of the enchantment")
          .translation(CONFIG_PREFIX + "levels")
          .defineInRange("levels", 1, 1, 5);

      isTreasure = builder.comment("Whether or not to consider this enchantment as a treasure")
          .translation(CONFIG_PREFIX + "isTreasure")
          .define("isTreasure", false);

      isVillagerTrade =
          builder.comment("Whether or not this enchantment can be offered by villagers for trade")
              .translation(CONFIG_PREFIX + "isVillagerTrade")
              .define("isVillagerTrade", true);

      isLootable = builder.comment("Whether or not this enchantment can generate in loot")
          .translation(CONFIG_PREFIX + "isLootable")
          .define("isLootable", true);

      canApplyAtEnchantingTable =
          builder.comment("Whether or not this enchantment can be applied at the enchanting table")
              .translation(CONFIG_PREFIX + "canApplyAtEnchantingTable")
              .define("canApplyAtEnchantingTable", true);

      canApplyOnBooks = builder.comment("Whether or not this enchantment can be applied on books")
          .translation(CONFIG_PREFIX + "canApplyOnBooks")
          .define("canApplyOnBooks", true);

      minEnchantabilityBase =
          builder.comment("The minimum enchantability requirement for the first enchantment level")
              .translation(CONFIG_PREFIX + "minEnchantabilityBase")
              .defineInRange("minEnchantabilityBase", 15, 1, 100);

      minEnchantabilityPerLevel = builder.comment(
              "The additional enchantability requirement for each enchantment level after the first")
          .translation(CONFIG_PREFIX + "minEnchantabilityPerLevel")
          .defineInRange("minEnchantabilityPerLevel", 5, 1, 100);

      incompatibleEnchantments = builder
          .comment("List of enchantments that cannot be applied together with this enchantment")
          .translation(CONFIG_PREFIX + "incompatibleEnchantments")
          .defineList("incompatibleEnchantments", new ArrayList<>(), s -> s instanceof String);

      items = builder.comment("List of items that the enchantment can be applied on")
          .translation(CONFIG_PREFIX + "items")
          .defineList("items", Arrays.asList("is:tool", "quark:pickarang", "quark:flamerang"),
              s -> s instanceof String);

      builder.pop();

      builder.push("vein mining");

      requireEffectiveTool =
          builder.comment("Whether or not to require an effective tool to vein mine blocks")
              .translation(CONFIG_PREFIX + "requireEffectiveTool")
              .define("requireEffectiveTool", false);

      maxBlocksBase =
          builder.comment("The maximum number of blocks to mine without the enchantment")
              .translation(CONFIG_PREFIX + "maxBlocksBase")
              .defineInRange("maxBlocksBase", 0, 0, 1000);

      maxDistanceBase =
          builder.comment("The maximum distance from the source block without the enchantment")
              .translation(CONFIG_PREFIX + "maxDistanceBase")
              .defineInRange("maxDistanceBase", 0, 0, 1000);

      maxBlocksPerLevel =
          builder.comment("The maximum number of blocks to mine per level of the enchantment")
              .translation(CONFIG_PREFIX + "maxBlocksPerLevel")
              .defineInRange("maxBlocksPerLevel", 50, 1, 1000);

      maxDistancePerLevel =
          builder.comment("The maximum distance from the source block per level of the enchantment")
              .translation(CONFIG_PREFIX + "maxDistancePerLevel")
              .defineInRange("maxDistancePerLevel", 15, 1, 100);

      diagonalMining =
          builder.comment(
                  "Whether or not to vein mine diagonally, note this may lead to hidden drops if relocateDrops is false")
              .translation(CONFIG_PREFIX + "diagonalMining")
              .define("diagonalMining", true);

      limitedByDurability =
          builder.comment("Whether or not to stop vein mining when the tool can no longer be used")
              .translation(CONFIG_PREFIX + "limitedByDurability")
              .define("limitedByDurability", true);

      relocateDrops = builder.comment("Whether or not to move all drops to the same location")
          .translation(CONFIG_PREFIX + "relocateDrops")
          .define("relocateDrops", true);

      preventToolDestruction =
          builder.comment("Whether or not the tool can break while mining additional blocks")
              .translation(CONFIG_PREFIX + "preventToolDestruction")
              .define("preventToolDestruction", true);

      addToolDamage =
          builder.comment("Whether or not the tool takes damage from mining additional blocks")
              .translation(CONFIG_PREFIX + "addToolDamage")
              .define("addToolDamage", true);

      toolDamageMultiplier =
          builder.comment("The multiplier to tool damage from mining additional blocks")
              .translation(CONFIG_PREFIX + "toolDamageMultiplier")
              .defineInRange("toolDamageMultiplier", 1, 0, 1000);

      addPlayerExhaustion =
          builder.comment("Whether or not the player gets exhaustion from mining additional blocks")
              .translation(CONFIG_PREFIX + "addPlayerExhaustion")
              .define("addPlayerExhaustion", true);

      playerExhaustionMultiplier =
          builder.comment("The multiplier to player exhaustion from mining additional blocks")
              .translation(CONFIG_PREFIX + "playerExhaustionMultiplier")
              .defineInRange("playerExhaustionMultiplier", 1.0F, 0.0F, 1000.0F);

      blocks = builder.comment("List of whitelisted/blacklisted blocks or block tags")
          .translation(CONFIG_PREFIX + "blocks")
          .defineList("blocks", new ArrayList<>(), s -> s instanceof String);

      blocksPermission =
          builder.comment("Whether the blocks configuration is a whitelist or a blacklist")
              .translation(CONFIG_PREFIX + "blocksPermission")
              .defineEnum("blocksPermission", PermissionType.BLACKLIST);

      builder.pop();

      builder.push("groups");

      groups = builder.comment("List of groupings by block IDs or block tags, comma-separated")
          .translation(CONFIG_PREFIX + "groups")
          .defineList("groups", generateDefaultGroups(), s -> s instanceof String);

      builder.pop();
    }
  }

  public static List<String> generateDefaultGroups() {
    return Lists.newArrayList(
        "#forge:obsidian",
        "#forge:ores/coal",
        "#forge:ores/diamond",
        "#forge:ores/emerald",
        "#forge:ores/gold",
        "#forge:ores/iron",
        "#forge:ores/lapis",
        "#forge:ores/redstone",
        "#forge:ores/quartz",
        "#forge:ores/netherite_scrap",
        "#forge:ores/copper",
        "#forge:ores/tin",
        "#forge:ores/osmium",
        "#forge:ores/uranium",
        "#forge:ores/fluorite",
        "#forge:ores/lead",
        "#forge:ores/zinc",
        "#forge:ores/aluminum",
        "#forge:ores/nickel",
        "#forge:ores/silver",
        "#forge:ores/apatite",
        "#forge:ores/cinnabar",
        "#forge:ores/niter",
        "#forge:ores/ruby",
        "#forge:ores/sapphire",
        "#forge:ores/sulfur"
    );
  }

  public enum PermissionType {
    BLACKLIST,
    WHITELIST
  }

  public enum ActivationState {
    STANDING,
    CROUCHING,
    KEYBINDING
  }
}
