package com.illusivesoulworks.veinmining.mixin.core;

import com.illusivesoulworks.veinmining.mixin.VeinMiningMixinHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Block.class)
public class MixinExperience {

  @ModifyVariable(at = @At("HEAD"), method = "popExperience", argsOnly = true)
  private BlockPos veinmining$popExperience(BlockPos pos, ServerLevel level, BlockPos unused,
                                            int amount) {
    return VeinMiningMixinHooks.getActualSpawnPos(level, pos);
  }
}
