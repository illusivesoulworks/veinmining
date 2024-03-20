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

package com.illusivesoulworks.veinmining.common.veinmining.logic;

import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.platform.Services;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockProcessor {

  private static final Map<String, Boolean> checkedBlocks = new HashMap<>();
  private static final Map<String, Map<String, Boolean>> checkedPairs = new HashMap<>();
  private static final Map<String, Set<String>> checkedTags = new HashMap<>();

  public static synchronized void rebuild() {
    checkedBlocks.clear();
    checkedPairs.clear();
    checkedTags.clear();
    BlockGroups.init();
  }

  public static boolean isValidTarget(BlockState state, Block source) {
    Block block = state.getBlock();
    return Services.PLATFORM.getResourceLocation(block).map(resourceLocation -> !state.isAir() &&
        checkedBlocks.computeIfAbsent(resourceLocation.toString(),
            (name) -> BlockProcessor.checkBlock(state)) && matches(source, block)).orElse(false);
  }

  private static boolean matches(Block origin, Block target) {

    if (origin == target) {
      return true;
    } else {
      String originName =
          Services.PLATFORM.getResourceLocation(origin).map(ResourceLocation::toString).orElse("");
      String targetName =
          Services.PLATFORM.getResourceLocation(target).map(ResourceLocation::toString).orElse("");

      if (originName.isEmpty() && targetName.isEmpty()) {
        return false;
      }
      boolean useOriginKey = originName.compareTo(targetName) >= 0;

      if (useOriginKey) {
        return checkedPairs.computeIfAbsent(originName, (name) -> new HashMap<>())
            .computeIfAbsent(targetName, (name) -> checkMatch(origin, target));
      } else {
        return checkedPairs.computeIfAbsent(targetName, (name) -> new HashMap<>())
            .computeIfAbsent(originName, (name) -> checkMatch(origin, target));
      }
    }
  }

  private static boolean checkBlock(BlockState blockState) {
    VeinMiningConfig.BlocksType blocksType = VeinMiningConfig.SERVER.blocks.get();

    if (blocksType == VeinMiningConfig.BlocksType.ALL) {
      return true;
    }

    if (blocksType == VeinMiningConfig.BlocksType.NO_BLOCK_ENTITIES) {
      return !blockState.hasBlockEntity();
    }
    String blockId =
        Services.PLATFORM.getResourceLocation(blockState.getBlock()).map(ResourceLocation::toString)
            .orElse("");

    if (blockId.isEmpty()) {
      return false;
    }
    Set<String> blockIds = new HashSet<>();
    blockIds.add(blockId);
    Set<String> tags = checkedTags.computeIfAbsent(blockId, (name) -> getTagsFor(blockState));
    tags.forEach(tag -> blockIds.add("#" + tag));
    boolean allow = true;
    Set<String> validIds = new HashSet<>();

    switch (blocksType) {
      case ORES -> {
        validIds.add("#c:ores");
        validIds.add("#forge:ores");
      }
      case ORES_LOGS -> {
        validIds.add("#c:ores");
        validIds.add("#forge:ores");
        validIds.add("#minecraft:logs");
      }
      case ORES_STONE -> {
        validIds.add("#c:ores");
        validIds.add("#forge:ores");
        validIds.add("#minecraft:base_stone_overworld");
        validIds.add("#minecraft:base_stone_nether");
      }
      case ORES_STONE_LOGS -> {
        validIds.add("#c:ores");
        validIds.add("#forge:ores");
        validIds.add("#minecraft:logs");
        validIds.add("#minecraft:base_stone_overworld");
        validIds.add("#minecraft:base_stone_nether");
      }
      case CONFIG_LIST -> {
        VeinMiningConfig.SERVER.blocksList.clearCache();
        Set<String> set = VeinMiningConfig.SERVER.blocksList.getTransformed();
        validIds.addAll(set);
        allow = VeinMiningConfig.SERVER.blocksListType.get() == VeinMiningConfig.ListType.ALLOW;
      }
    }

    for (String id : validIds) {

      if (blockIds.contains(id)) {
        return allow;
      }
    }
    return !allow;
  }

  private static Set<String> getTagsFor(BlockState blockState) {
    Set<String> tags = new HashSet<>();
    BuiltInRegistries.BLOCK.getTagNames().forEach(blockTagKey -> {

      if (blockState.is(blockTagKey)) {
        tags.add(blockTagKey.location().toString());
      }
    });
    return tags;
  }

  private static boolean checkMatch(Block origin, Block target) {
    String originName =
        Services.PLATFORM.getResourceLocation(origin).map(ResourceLocation::toString).orElse("");
    String targetName =
        Services.PLATFORM.getResourceLocation(target).map(ResourceLocation::toString).orElse("");

    if (originName.isEmpty() || targetName.isEmpty()) {
      return false;
    }
    return BlockGroups.getGroup(originName).contains(targetName);
  }
}
