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

package top.theillusivec4.veinmining.veinmining.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class BlockProcessor {

  private static final Map<String, Boolean> checkedBlocks = new HashMap<>();
  private static final Map<String, Map<String, Boolean>> checkedPairs = new HashMap<>();

  public static synchronized void rebuild() {
    checkedBlocks.clear();
    checkedPairs.clear();
    BlockGroups.init();
  }

  public static boolean isValidTarget(BlockState state, Level world, BlockPos pos, Block source) {
    Block block = state.getBlock();
    return !state.isAir() &&
        checkedBlocks.computeIfAbsent(Objects.requireNonNull(block.getRegistryName()).toString(),
            (name) -> BlockProcessor.checkBlock(block)) && matches(source, block);
  }

  private static boolean matches(Block origin, Block target) {

    if (origin == target) {
      return true;
    } else {
      String originName = Objects.requireNonNull(origin.getRegistryName()).toString();
      String targetName = Objects.requireNonNull(target.getRegistryName()).toString();
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

  private static boolean checkBlock(Block block) {
    Set<String> ids = new HashSet<>();
    ids.add(Objects.requireNonNull(block.getRegistryName()).toString());
    block.getTags().forEach(tag -> ids.add("#" + tag.toString()));
    Set<String> configs = VeinMiningConfig.VeinMining.blocks;

    if (VeinMiningConfig.VeinMining.blocksPermission ==
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

  private static boolean checkMatch(Block origin, Block target) {
    Set<String> group =
        BlockGroups.getGroup(Objects.requireNonNull(origin.getRegistryName()).toString());

    if (group != null) {
      return group.contains(Objects.requireNonNull(target.getRegistryName()).toString());
    }
    return false;
  }
}
