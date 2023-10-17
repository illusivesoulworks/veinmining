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

import com.google.common.collect.Sets;
import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.platform.Services;
import java.util.LinkedList;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class VeinMiningLogic {

  private static final Direction[] CARDINAL_DIRECTIONS =
      new Direction[] {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST,
          Direction.NORTH, Direction.SOUTH};

  public static void veinMine(ServerPlayer playerEntity, BlockPos pos, BlockState sourceState) {
    ServerLevel world = playerEntity.serverLevel();
    ItemStack stack = playerEntity.getMainHandItem();
    Block source = sourceState.getBlock();
    boolean notCorrect = VeinMiningConfig.SERVER.requireCorrectTool.get() &&
        !Services.PLATFORM.canHarvestDrops(playerEntity, sourceState);
    boolean tooSlow =
        stack.getDestroySpeed(sourceState) < VeinMiningConfig.SERVER.requiredDestroySpeed.get();

    if (notCorrect || tooSlow) {
      return;
    }
    int veiningLevels =
        EnchantmentHelper.getItemEnchantmentLevel(Services.PLATFORM.getVeinMiningEnchantment(),
            stack);
    int maxBlocks = VeinMiningConfig.SERVER.maxBlocksBase.get() +
        VeinMiningConfig.SERVER.maxBlocksPerLevel.get() * veiningLevels;
    int maxDistance = 200;

    if (maxBlocks <= 0) {
      return;
    }
    int blocks = 1;
    Set<BlockPos> visited = Sets.newHashSet(pos);
    LinkedList<Tuple<BlockPos, Integer>> candidates = new LinkedList<>();
    addValidNeighbors(candidates, pos, 1);

    while (!candidates.isEmpty() && blocks < maxBlocks) {
      Tuple<BlockPos, Integer> candidate = candidates.poll();
      BlockPos blockPos = candidate.getA();
      int blockDistance = candidate.getB();

      if (stopVeining(stack)) {
        return;
      }
      BlockState blockState = world.getBlockState(blockPos);

      if (visited.add(blockPos) && BlockProcessor.isValidTarget(blockState, source) &&
          Services.PLATFORM.harvest(playerEntity, blockPos, pos)) {

        if (blockDistance < maxDistance) {
          addValidNeighbors(candidates, blockPos, blockDistance + 1);
        }
        blocks++;
      }
    }
  }

  private static boolean stopVeining(ItemStack stack) {
    return stack.isDamageableItem() && VeinMiningConfig.SERVER.limitedByDurability.get() &&
        (stack.getDamageValue() == stack.getMaxDamage() ||
            (VeinMiningConfig.SERVER.preventToolDestruction.get() &&
                stack.getDamageValue() == stack.getMaxDamage() - 1));
  }

  private static void addValidNeighbors(LinkedList<Tuple<BlockPos, Integer>> candidates,
                                        BlockPos source, int distance) {

    if (VeinMiningConfig.SERVER.diagonalMining.get()) {
      BlockPos up = source.above();
      BlockPos down = source.below();
      candidates.add(new Tuple<>(up, distance));
      candidates.add(new Tuple<>(down, distance));
      BlockPos[] blockPositions = new BlockPos[] {up, down, source};

      for (BlockPos blockPos : blockPositions) {
        candidates.add(new Tuple<>(blockPos.west(), distance));
        candidates.add(new Tuple<>(blockPos.east(), distance));
        candidates.add(new Tuple<>(blockPos.north(), distance));
        candidates.add(new Tuple<>(blockPos.south(), distance));
        candidates.add(new Tuple<>(blockPos.north().west(), distance));
        candidates.add(new Tuple<>(blockPos.north().east(), distance));
        candidates.add(new Tuple<>(blockPos.south().west(), distance));
        candidates.add(new Tuple<>(blockPos.south().east(), distance));
      }
    } else {

      for (Direction direction : CARDINAL_DIRECTIONS) {
        candidates.add(new Tuple<>(source.relative(direction), distance));
      }
    }
  }
}
