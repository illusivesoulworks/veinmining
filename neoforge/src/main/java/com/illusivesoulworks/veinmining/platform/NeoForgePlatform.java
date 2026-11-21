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

package com.illusivesoulworks.veinmining.platform;

import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.network.SPacketNotify;
import com.illusivesoulworks.veinmining.common.platform.services.IPlatform;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningPlayers;
import com.illusivesoulworks.veinmining.mixin.core.AccessorFoodData;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class NeoForgePlatform implements IPlatform {

  @Override
  public void sendNotifyS2C(ServerPlayer player) {
    PacketDistributor.sendToPlayer(player, SPacketNotify.INSTANCE);
  }

  @Override
  public Set<String> getBlocksFromTag(Identifier identifier) {
    Set<String> result = new HashSet<>();
    BuiltInRegistries.BLOCK.get(TagKey.create(Registries.BLOCK, identifier))
        .ifPresent(block -> {
          for (Holder<Block> blockHolder : block) {
            blockHolder.unwrapKey().ifPresent(key -> result.add(key.identifier().toString()));
          }
        });
    return result;
  }

  @Override
  public Block getBlock(Identifier identifier) {
    return BuiltInRegistries.BLOCK.getValue(identifier);
  }

  @Override
  public Optional<Identifier> getResourceLocation(Block block) {
    return Optional.of(BuiltInRegistries.BLOCK.getKey(block));
  }

  @Override
  public boolean canHarvestDrops(ServerPlayer playerEntity, BlockState state, BlockPos pos) {
    return EventHooks.doPlayerHarvestCheck(playerEntity, state, playerEntity.level(), pos);
  }

  // Mirrors ServerPlayerGameMode#destroyBlock(BlockPos) from NeoForge 26.1, with drop relocation
  // and exhaustion scaling added.
  public boolean harvest(ServerPlayer player, BlockPos pos, BlockPos originPos) {
    ServerLevel world = player.level();
    BlockState blockstate = world.getBlockState(pos);
    GameType gameType = player.gameMode.getGameModeForPlayer();
    BreakBlockEvent evt = CommonHooks.fireBlockBreak(world, gameType, player, pos, blockstate);

    if (evt.isCanceled()) {
      return false;
    } else {
      BlockEntity blockentity = world.getBlockEntity(pos);
      Block block = blockstate.getBlock();

      if (block instanceof GameMasterBlock && !player.canUseGameMasterBlocks()) {
        world.sendBlockUpdated(pos, blockstate, blockstate, 3);
        return false;
      } else if (player.blockActionRestricted(world, pos, gameType)) {
        return false;
      } else {
        BlockState adjustedState = block.playerWillDestroy(world, pos, blockstate, player);

        if (player.preventsBlockDrops()) {
          removeBlock(player, world, pos, adjustedState, false, player.getMainHandItem().copy());
        } else {
          ItemStack itemstack = player.getMainHandItem();
          ItemStack itemstack1 = itemstack.copy();
          boolean flag1 = adjustedState.canHarvestBlock(world, pos, player);
          itemstack.mineBlock(world, adjustedState, pos, player);
          boolean flag = removeBlock(player, world, pos, adjustedState, flag1, itemstack1.copy());
          BlockPos spawnPos = VeinMiningConfig.SERVER.relocateDrops.get() ? originPos : pos;

          if (flag && flag1) {
            FoodData foodData = player.getFoodData();
            AccessorFoodData accessor = (AccessorFoodData) foodData;
            float currentExhaustion = accessor.getExhaustionLevel();
            VeinMiningPlayers.addMiningBlock(world, pos, spawnPos);
            block.playerDestroy(world, player, pos, adjustedState, blockentity, itemstack1);
            VeinMiningPlayers.removeMiningBlock(world, pos);

            if (VeinMiningConfig.SERVER.addExhaustion.get()) {
              float diff = accessor.getExhaustionLevel() - currentExhaustion;
              accessor.setExhaustionLevel(currentExhaustion);
              foodData.addExhaustion(
                  (float) (diff * VeinMiningConfig.SERVER.exhaustionMultiplier.get()));
            } else {
              accessor.setExhaustionLevel(currentExhaustion);
            }
          }

          if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
            EventHooks.onPlayerDestroyItem(player, itemstack1, InteractionHand.MAIN_HAND);
          }
        }
        return true;
      }
    }
  }

  private static boolean removeBlock(ServerPlayer player, ServerLevel world, BlockPos pos,
                                     BlockState state, boolean canHarvest, ItemStack toolStack) {
    boolean removed = state.onDestroyedByPlayer(world, pos, player, toolStack, canHarvest,
        world.getFluidState(pos));

    if (removed) {
      state.getBlock().destroy(world, pos, state);
    }
    return removed;
  }
}
