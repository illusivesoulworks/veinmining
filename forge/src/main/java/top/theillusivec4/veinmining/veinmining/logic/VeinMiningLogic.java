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

import com.google.common.collect.Sets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.capabilities.IVeinCapability;
import top.theillusivec4.veinmining.capabilities.VeinCapability;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;

public class VeinMiningLogic {
  
  static boolean exit = false;
  
  private final static Map<Player, LazyOptional<IVeinCapability>> cache = new HashMap<>();

  private static final Direction[] CARDINAL_DIRECTIONS =
      new Direction[] {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST,
          Direction.NORTH, Direction.SOUTH};

  public static void startVeinMining(ServerPlayer playerEntity, BlockPos pos, Block source) {
	  
	
	LazyOptional<IVeinCapability> tCap = cache.get(playerEntity);
	
	if (tCap == null) {
		tCap = playerEntity.getCapability(VeinCapability.INSTANCE);
		cache.put(playerEntity, tCap);
		tCap.addListener(self -> cache.put(playerEntity, null));
	}
	
	
	tCap.ifPresent(get -> {exit = get.isVeining();});
	
	if (exit) {
		return;
	}
	
    ServerLevel world = playerEntity.getLevel();
    ItemStack stack = playerEntity.getMainHandItem();

    if (!VeinMiningPlayers.canVeinMine(playerEntity)) {
      return;
    }
    BlockState state = world.getBlockState(pos);
    boolean ineffective =
        VeinMiningConfig.VeinMining.requireEffectiveTool &&
            !ForgeHooks.isCorrectToolForDrops(state, playerEntity);

    if (ineffective) {
      return;
    }
    int veiningLevels = EnchantmentHelper.getItemEnchantmentLevel(VeinMiningMod.VEIN_MINING, stack);
    int maxBlocks = VeinMiningConfig.VeinMining.maxBlocksBase +
        VeinMiningConfig.VeinMining.maxBlocksPerLevel * veiningLevels;
    int maxDistance = VeinMiningConfig.VeinMining.maxDistanceBase +
        VeinMiningConfig.VeinMining.maxDistancePerLevel * veiningLevels;

    if (maxBlocks <= 0 || maxDistance <= 0) {
      return;
    }
    int blocks = 0;
    Set<BlockPos> visited = Sets.newHashSet(pos);
    LinkedList<Tuple<BlockPos, Integer>> candidates = new LinkedList<>();
    addValidNeighbors(candidates, pos, 1);
    
    tCap.ifPresent(set -> set.setVeining(true));

    while (!candidates.isEmpty() && blocks < maxBlocks) {
      Tuple<BlockPos, Integer> candidate = candidates.poll();
      BlockPos blockPos = candidate.getA();
      int blockDistance = candidate.getB();

      if (stopVeining(stack)) {
    	tCap.ifPresent(set -> set.setVeining(false));
        return;
      }
      BlockState blockState = world.getBlockState(blockPos);

      if (visited.add(blockPos) &&
          BlockProcessor.isValidTarget(blockState, world, blockPos, source) &&
          harvest(playerEntity, blockPos, pos)) {

        if (blockDistance < maxDistance) {
          addValidNeighbors(candidates, blockPos, blockDistance + 1);
        }
        blocks++;
      }
    }
    tCap.ifPresent(set -> set.setVeining(false));
  }

  private static boolean stopVeining(ItemStack stack) {
    return VeinMiningConfig.VeinMining.limitedByDurability &&
        (stack.getDamageValue() == stack.getMaxDamage() ||
            (VeinMiningConfig.VeinMining.preventToolDestruction &&
                stack.getDamageValue() == stack.getMaxDamage() - 1));
  }

  private static void addValidNeighbors(LinkedList<Tuple<BlockPos, Integer>> candidates,
                                        BlockPos source, int distance) {

    if (VeinMiningConfig.VeinMining.diagonalMining) {
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

  public static boolean harvest(ServerPlayer player, BlockPos pos, BlockPos originPos) {
    ServerLevel world = player.getLevel();
    BlockState blockstate = world.getBlockState(pos);
    GameType gameType = player.gameMode.getGameModeForPlayer();
    int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(world, gameType, player, pos);

    if (exp == -1) {
      return false;
    } else {
      BlockEntity tileentity = world.getBlockEntity(pos);
      Block block = blockstate.getBlock();

      if ((block instanceof CommandBlock || block instanceof StructureBlock ||
          block instanceof JigsawBlock) && !player.canUseGameMasterBlocks()) {
        world.sendBlockUpdated(pos, blockstate, blockstate, 3);
        return false;
      } else if (player.getMainHandItem().onBlockStartBreak(pos, player)) {
        return false;
      } else if (player.blockActionRestricted(world, pos, gameType)) {
        return false;
      } else {

        if (gameType.isCreative()) {
          removeBlock(player, pos, false);
        } else {
          ItemStack itemstack = player.getMainHandItem();
          ItemStack itemstack1 = itemstack.copy();
          boolean flag1 = blockstate.canHarvestBlock(world, pos, player);

          if (VeinMiningConfig.VeinMining.addToolDamage) {
            onBlockDestroyed(itemstack, world, blockstate, pos, player);
          }

          if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
            net.minecraftforge.event.ForgeEventFactory
                .onPlayerDestroyItem(player, itemstack1, InteractionHand.MAIN_HAND);
          }
          boolean flag = removeBlock(player, pos, flag1);
          BlockPos spawnPos = VeinMiningConfig.VeinMining.relocateDrops ? originPos : pos;

          if (flag && flag1) {
            harvestBlock(block, world, player, pos, spawnPos, blockstate, tileentity, itemstack1);
          }

          if (flag && exp > 0) {
            blockstate.getBlock().popExperience(world, spawnPos, exp);
          }
        }
        return true;
      }
    }
  }

  private static void onBlockDestroyed(ItemStack stack, Level worldIn, BlockState blockIn,
                                       BlockPos pos, Player playerIn) {

    if (!worldIn.isClientSide && blockIn.getDestroySpeed(worldIn, pos) != 0.0F) {
      int damage = VeinMiningConfig.VeinMining.toolDamageMultiplier;

      if (VeinMiningConfig.VeinMining.preventToolDestruction) {
        damage = Math.min(damage, stack.getMaxDamage() - stack.getDamageValue() - 2);
      }

      if (damage > 0) {
        stack.hurtAndBreak(damage, playerIn,
            (entity) -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
      }
    }
  }

  private static void harvestBlock(Block block, Level worldIn, Player player, BlockPos pos,
                                   BlockPos spawnPos, BlockState state, @Nullable BlockEntity te,
                                   ItemStack stack) {
    player.awardStat(Stats.BLOCK_MINED.get(block));

    if (VeinMiningConfig.VeinMining.addPlayerExhaustion) {
      player.causeFoodExhaustion(
          (float) (0.005F * (VeinMiningConfig.VeinMining.playerExhaustionMultiplier)));
    }

    if (worldIn instanceof ServerLevel) {
      Block.getDrops(state, (ServerLevel) worldIn, pos, te, player, stack)
          .forEach((stackToSpawn) -> Block.popResource(worldIn, spawnPos, stackToSpawn));
      state.spawnAfterBreak((ServerLevel) worldIn, pos, stack);
    }
  }

  private static boolean removeBlock(Player player, BlockPos pos, boolean canHarvest) {
    Level world = player.getCommandSenderWorld();
    BlockState state = world.getBlockState(pos);
    boolean removed =
        state.onDestroyedByPlayer(world, pos, player, canHarvest, world.getFluidState(pos));

    if (removed) {
      state.getBlock().destroy(world, pos, state);

      if (!world.getBlockState(pos).isAir()) {
        world.removeBlock(pos, false);
      }
    }
    return removed;
  }
}
