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

package top.theillusivec4.veinmining.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.veinmining.veinmining.logic.VeinMiningLogic;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {

  @Shadow
  public ServerPlayer player;

  private Block source = Blocks.AIR;

  @Inject(
      at = @At(value = "HEAD"),
      method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z")
  private void veinmining$preMineBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
    source = player.level.getBlockState(pos).getBlock();
  }

  @Inject(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/world/item/ItemStack.mineBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/player/Player;)V"),
      method = "destroyBlock(Lnet/minecraft/core/BlockPos;)Z")
  private void veinmining$mineBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
    VeinMiningLogic.startVeinMining(player, pos, source);
  }
}
