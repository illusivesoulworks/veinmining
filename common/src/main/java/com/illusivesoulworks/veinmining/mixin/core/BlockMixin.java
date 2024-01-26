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

package com.illusivesoulworks.veinmining.mixin.core;

import com.illusivesoulworks.veinmining.mixin.VeinMiningMixinHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Block.class)
public class BlockMixin {

  @ModifyVariable(at = @At("HEAD"), method = "popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V", argsOnly = true)
  private static BlockPos veinmining$popResource(BlockPos pos, Level level, BlockPos unused,
                                                 ItemStack stack) {
    return VeinMiningMixinHooks.getActualSpawnPos(level, pos);
  }

  @ModifyVariable(at = @At("HEAD"), method = "popResourceFromFace", argsOnly = true)
  private static BlockPos veinmining$popResourceFromFace(BlockPos pos, Level level, BlockPos unused,
                                                         Direction direction, ItemStack stack) {
    return VeinMiningMixinHooks.getActualSpawnPos(level, pos);
  }
}
