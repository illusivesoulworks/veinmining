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
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Registry;
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
    Set<String> ids = new HashSet<>();
    String blockId =
        Services.PLATFORM.getResourceLocation(blockState.getBlock()).map(ResourceLocation::toString)
            .orElse("");

    if (blockId.isEmpty()) {
      return false;
    }
    ids.add(blockId);
    Set<String> tags = checkedTags.computeIfAbsent(blockId, (name) -> getTagsFor(blockState));
    tags.forEach(tag -> ids.add("#" + tag));
    VeinMiningConfig.SERVER.blocks.clearCache();
    Set<String> configs = VeinMiningConfig.SERVER.blocks.getTransformed();

    if (VeinMiningConfig.SERVER.blocksPermission.get() ==
        VeinMiningConfig.PermissionType.BLACKLIST) {

      for (String id : configs) {

        if (ids.contains(id)) {
          return false;
        }
      }
      return true;
    } else {

      for (String id : configs) {

        if (ids.contains(id)) {
          return true;
        }
      }
      return false;
    }
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
