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

import com.google.common.collect.Sets;
import java.util.LinkedList;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;

public class VeinMiningLogic {

  private static final Direction[] CARDINAL_DIRECTIONS =
      new Direction[] {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST,
          Direction.NORTH, Direction.SOUTH};

  public static void startVeinMining(ServerPlayerEntity playerEntity, BlockPos pos,
                                     BlockState source) {
    ServerWorld world = playerEntity.getWorld();
    ItemStack stack = playerEntity.getMainHandStack();

    if (!VeinMiningPlayers.canVeinMine(playerEntity)) {
      return;
    }
    boolean ineffective = VeinMiningConfig.VeinMining.requireEffectiveTool &&
        !playerEntity.canHarvest(source);

    if (ineffective) {
      return;
    }
    int veiningLevels = EnchantmentHelper.getLevel(VeinMiningMod.VEIN_MINING, stack);
    int maxBlocks = VeinMiningConfig.VeinMining.maxBlocksBase +
        VeinMiningConfig.VeinMining.maxBlocksPerLevel * veiningLevels;
    int maxDistance = VeinMiningConfig.VeinMining.maxDistanceBase +
        VeinMiningConfig.VeinMining.maxDistancePerLevel * veiningLevels;

    if (maxBlocks <= 0 || maxDistance <= 0) {
      return;
    }
    int blocks = 0;
    Set<BlockPos> visited = Sets.newHashSet(pos);
    LinkedList<Pair<BlockPos, Integer>> candidates = new LinkedList<>();
    addValidNeighbors(candidates, pos, 1);
    Block sourceBlock = source.getBlock();

    while (!candidates.isEmpty() && blocks < maxBlocks) {
      Pair<BlockPos, Integer> candidate = candidates.poll();
      BlockPos blockPos = candidate.getLeft();
      int blockDistance = candidate.getRight();

      if (stopVeining(stack)) {
        return;
      }
      BlockState state = world.getBlockState(blockPos);

      if (visited.add(blockPos) &&
          BlockProcessor.isValidTarget(state, world, blockPos, sourceBlock) &&
          harvest(playerEntity, blockPos, pos)) {

        if (blockDistance < maxDistance) {
          addValidNeighbors(candidates, blockPos, blockDistance + 1);
        }
        blocks++;
      }
    }
  }

  private static boolean stopVeining(ItemStack stack) {
    return VeinMiningConfig.VeinMining.limitedByDurability &&
        (stack.getDamage() == stack.getMaxDamage() ||
            (VeinMiningConfig.VeinMining.preventToolDestruction &&
                stack.getDamage() == stack.getMaxDamage() - 1));
  }

  private static void addValidNeighbors(LinkedList<Pair<BlockPos, Integer>> candidates,
                                        BlockPos source, int distance) {

    if (VeinMiningConfig.VeinMining.diagonalMining) {
      BlockPos up = source.up();
      BlockPos down = source.down();
      candidates.add(new Pair<>(up, distance));
      candidates.add(new Pair<>(down, distance));
      BlockPos[] blockPositions = new BlockPos[] {up, down, source};

      for (BlockPos blockPos : blockPositions) {
        candidates.add(new Pair<>(blockPos.west(), distance));
        candidates.add(new Pair<>(blockPos.east(), distance));
        candidates.add(new Pair<>(blockPos.north(), distance));
        candidates.add(new Pair<>(blockPos.south(), distance));
        candidates.add(new Pair<>(blockPos.north().west(), distance));
        candidates.add(new Pair<>(blockPos.north().east(), distance));
        candidates.add(new Pair<>(blockPos.south().west(), distance));
        candidates.add(new Pair<>(blockPos.south().east(), distance));
      }
    } else {

      for (Direction direction : CARDINAL_DIRECTIONS) {
        candidates.add(new Pair<>(source.offset(direction), distance));
      }
    }
  }

  public static boolean harvest(ServerPlayerEntity player, BlockPos pos, BlockPos originPos) {
    ServerWorld world = player.getWorld();
    BlockState blockState = world.getBlockState(pos);
    GameMode gameMode = player.interactionManager.getGameMode();

    if (!player.getMainHandStack().getItem().canMine(blockState, world, pos, player)) {
      return false;
    } else {
      BlockEntity blockEntity = world.getBlockEntity(pos);
      Block block = blockState.getBlock();

      if ((block instanceof CommandBlock || block instanceof StructureBlock ||
          block instanceof JigsawBlock) && !player.isCreativeLevelTwoOp()) {
        world.updateListeners(pos, blockState, blockState, 3);
        return false;
      } else if (player.isBlockBreakingRestricted(world, pos, gameMode)) {
        return false;
      } else {
        block.onBreak(world, pos, blockState, player);
        boolean bl = world.removeBlock(pos, false);

        if (bl) {
          block.onBroken(world, pos, blockState);
        }

        if (gameMode != GameMode.CREATIVE) {
          ItemStack itemStack = player.getMainHandStack();
          ItemStack itemStack2 = itemStack.copy();
          boolean bl2 = player.canHarvest(blockState);

          if (VeinMiningConfig.VeinMining.addToolDamage) {
            postMine(itemStack, world, blockState, pos, player);
          }
          BlockPos spawnPos = VeinMiningConfig.VeinMining.relocateDrops ? originPos : pos;

          if (bl && bl2) {
            afterBreak(block, world, player, pos, spawnPos, blockState, blockEntity, itemStack2);
          }
        }
        return true;
      }
    }
  }

  private static void postMine(ItemStack stack, World world, BlockState state, BlockPos pos,
                               PlayerEntity miner) {

    if (!world.isClient && state.getHardness(world, pos) != 0.0F) {
      int damage = VeinMiningConfig.VeinMining.toolDamageMultiplier;

      if (VeinMiningConfig.VeinMining.preventToolDestruction) {
        damage = Math.min(damage, stack.getMaxDamage() - stack.getDamage() - 2);
      }

      if (damage > 0) {
        stack.damage(1, miner, (e) -> e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND));
      }
    }
  }

  private static void afterBreak(Block block, World world, PlayerEntity player, BlockPos pos,
                                 BlockPos spawnPos, BlockState state, BlockEntity blockEntity,
                                 ItemStack stack) {
    player.incrementStat(Stats.MINED.getOrCreateStat(block));

    if (VeinMiningConfig.VeinMining.addPlayerExhaustion) {
      player.addExhaustion(
          (float) (0.005F * VeinMiningConfig.VeinMining.playerExhaustionMultiplier));
    }
    dropStacks(state, world, pos, spawnPos, blockEntity, player, stack);
  }

  private static void dropStacks(BlockState state, World world, BlockPos pos, BlockPos spawnPos,
                                 BlockEntity blockEntity, Entity entity, ItemStack stack) {

    if (world instanceof ServerWorld) {
      Block.getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, entity, stack)
          .forEach((itemStack) -> Block.dropStack(world, spawnPos, itemStack));
      state.onStacksDropped((ServerWorld) world, pos, stack);
    }
  }
}
