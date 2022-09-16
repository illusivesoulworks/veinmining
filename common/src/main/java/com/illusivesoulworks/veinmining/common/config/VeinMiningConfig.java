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

package com.illusivesoulworks.veinmining.common.config;

import com.google.common.collect.Lists;
import com.illusivesoulworks.spectrelib.config.SpectreConfigSpec;
import com.illusivesoulworks.veinmining.VeinMiningConstants;
import com.illusivesoulworks.veinmining.common.platform.Services;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.tuple.Pair;

public class VeinMiningConfig {

  public static final SpectreConfigSpec SERVER_SPEC;
  public static final SpectreConfigSpec COMMON_SPEC;
  public static final SpectreConfigSpec CLIENT_SPEC;
  public static final Server SERVER;
  public static final Common COMMON;
  public static final Client CLIENT;
  private static final String CONFIG_PREFIX = "gui." + VeinMiningConstants.MOD_ID + ".config.";

  static {
    final Pair<Server, SpectreConfigSpec> specPairServer = new SpectreConfigSpec.Builder()
        .configure(Server::new);
    SERVER_SPEC = specPairServer.getRight();
    SERVER = specPairServer.getLeft();
    final Pair<Common, SpectreConfigSpec> specPairCommon = new SpectreConfigSpec.Builder()
        .configure(Common::new);
    COMMON_SPEC = specPairCommon.getRight();
    COMMON = specPairCommon.getLeft();
    final Pair<Client, SpectreConfigSpec> specPairClient = new SpectreConfigSpec.Builder()
        .configure(Client::new);
    CLIENT_SPEC = specPairClient.getRight();
    CLIENT = specPairClient.getLeft();
  }

  public static class Server {

    public final SpectreConfigSpec.BooleanValue requireEffectiveTool;
    public final SpectreConfigSpec.IntValue maxBlocksBase;
    public final SpectreConfigSpec.IntValue maxDistanceBase;
    public final SpectreConfigSpec.IntValue maxBlocksPerLevel;
    public final SpectreConfigSpec.IntValue maxDistancePerLevel;
    public final SpectreConfigSpec.BooleanValue diagonalMining;
    public final SpectreConfigSpec.BooleanValue relocateDrops;
    public final SpectreConfigSpec.BooleanValue preventToolDestruction;
    public final SpectreConfigSpec.BooleanValue addToolDamage;
    public final SpectreConfigSpec.IntValue toolDamageMultiplier;
    public final SpectreConfigSpec.BooleanValue addPlayerExhaustion;
    public final SpectreConfigSpec.DoubleValue playerExhaustionMultiplier;
    public final SpectreConfigSpec.BooleanValue limitedByDurability;
    public final SpectreConfigSpec.TransformableValue<List<? extends String>, Set<String>> blocks;
    public final SpectreConfigSpec.EnumValue<PermissionType> blocksPermission;

    public final SpectreConfigSpec.TransformableValue<List<? extends String>, Set<String>> groups;

    public Server(SpectreConfigSpec.Builder builder) {
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
          .defineList("blocks", new ArrayList<>(), s -> s instanceof String, Set::copyOf);

      blocksPermission =
          builder.comment("Whether the blocks configuration is a whitelist or a blacklist")
              .translation(CONFIG_PREFIX + "blocksPermission")
              .defineEnum("blocksPermission", PermissionType.BLACKLIST);

      builder.pop();

      builder.push("groups");

      groups = builder.comment("List of groupings by block IDs or block tags, comma-separated")
          .translation(CONFIG_PREFIX + "groups")
          .defineList("groups", generateDefaultGroups(), s -> s instanceof String, Set::copyOf);

      builder.pop();
    }
  }

  public static class Common {

    public final SpectreConfigSpec.EnumValue<Enchantment.Rarity> rarity;
    public final SpectreConfigSpec.IntValue levels;
    public final SpectreConfigSpec.BooleanValue isTreasure;
    public final SpectreConfigSpec.BooleanValue isVillagerTrade;
    public final SpectreConfigSpec.BooleanValue isLootable;
    public final SpectreConfigSpec.BooleanValue canApplyAtEnchantingTable;
    public final SpectreConfigSpec.BooleanValue canApplyOnBooks;
    public final SpectreConfigSpec.IntValue minEnchantabilityBase;
    public final SpectreConfigSpec.IntValue minEnchantabilityPerLevel;
    public final SpectreConfigSpec.TransformableValue<List<? extends String>, Set<Enchantment>>
        incompatibleEnchantments;
    public final SpectreConfigSpec.TransformableValue<List<? extends String>, Set<String>>
        items;

    public Common(SpectreConfigSpec.Builder builder) {
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
          .defineListAllowEmpty(List.of("incompatibleEnchantments"), ArrayList::new,
              s -> s instanceof String,
              (Function<List<? extends String>, Set<Enchantment>>) this::convertEnchantments);

      items = builder.comment("List of items that the enchantment can be applied on")
          .translation(CONFIG_PREFIX + "items")
          .defineList("items", Services.PLATFORM.getDefaultItemsConfig(), s -> s instanceof String,
              Set::copyOf);

      builder.pop();
    }

    private Set<Enchantment> convertEnchantments(List<? extends String> input) {
      Set<Enchantment> result = new HashSet<>();

      for (String s : input) {
        Services.PLATFORM.getEnchantment(ResourceLocation.tryParse(s)).ifPresent(result::add);
      }
      return result;
    }
  }

  public static class Client {

    public final SpectreConfigSpec.EnumValue<VeinMiningConfig.ActivationState> activationState;
    public final SpectreConfigSpec.EnumValue<VeinMiningConfig.ActivationState>
        activationStateWithoutEnchantment;

    public Client(SpectreConfigSpec.Builder builder) {
      builder.push("vein mining");

      activationState = builder.comment(
              "Whether to activate vein mining (if using with the enchantment) by standing, crouching, or holding down the keybind")
          .translation(CONFIG_PREFIX + "activationState")
          .defineEnum("activationState", VeinMiningConfig.ActivationState.STANDING);

      activationStateWithoutEnchantment = builder.comment(
              "Whether to activate vein mining (if using without the enchantment) by standing, crouching, or holding down the keybind")
          .translation(CONFIG_PREFIX + "activationStateWithoutEnchantment")
          .defineEnum("activationStateWithoutEnchantment",
              VeinMiningConfig.ActivationState.KEYBINDING);

      builder.pop();
    }
  }

  public static List<String> generateDefaultGroups() {
    return Services.PLATFORM.getDefaultGroups();
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
