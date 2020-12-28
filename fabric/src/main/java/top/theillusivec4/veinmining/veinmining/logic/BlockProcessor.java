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

package top.theillusivec4.veinmining.veinmining.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class BlockProcessor {

  private static final Map<String, Boolean> checkedBlocks = new HashMap<>();
  private static final Map<String, Map<String, Boolean>> checkedPairs = new HashMap<>();
  private static final Map<String, Set<String>> checkedTags = new HashMap<>();

  public static void rebuild() {
    checkedBlocks.clear();
    checkedPairs.clear();
    checkedTags.clear();
    BlockGroups.init();
  }

  public static boolean isValidTarget(BlockState state, ServerWorld world, BlockPos pos,
                                      Block source) {
    Block block = world.getBlockState(pos).getBlock();
    return !state.isAir() && checkedBlocks.computeIfAbsent(Registry.BLOCK.getId(block).toString(),
        (name) -> BlockProcessor.checkBlock(world, block)) && matches(source, block);
  }

  private static boolean matches(Block origin, Block target) {

    if (origin == target) {
      return true;
    } else {
      String originName = Registry.BLOCK.getId(origin).toString();
      String targetName = Registry.BLOCK.getId(target).toString();
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

  private static boolean checkBlock(ServerWorld world, Block block) {
    Set<String> ids = new HashSet<>();
    String blockId = Registry.BLOCK.getId(block).toString();
    ids.add(blockId);
    Set<String> tags = checkedTags.computeIfAbsent(blockId, (name) -> getTagsFor(world, block));
    tags.forEach(tag -> ids.add("#" + tag));
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

  private static Set<String> getTagsFor(ServerWorld world, Block block) {
    TagGroup<Block> tagGroup = world.getTagManager().getBlocks();
    Set<String> tags = new HashSet<>();

    for (Map.Entry<Identifier, Tag<Block>> entry : tagGroup.getTags().entrySet()) {

      if ((entry.getValue()).contains(block)) {
        tags.add(entry.getKey().toString());
      }
    }
    return tags;
  }

  private static boolean checkMatch(Block origin, Block target) {
    Set<String> group =
        BlockGroups.getGroup(Registry.BLOCK.getId(origin).toString());

    if (group != null) {
      return group.contains(Registry.BLOCK.getId(target).toString());
    }
    return false;
  }
}
