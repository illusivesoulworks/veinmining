package top.theillusivec4.veinmining.mixin.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.veinmining.mixin.VeinMiningMixinHooks;

@Mixin(Block.class)
public class BlockMixin {

  @ModifyVariable(at = @At("HEAD"), method = "popResource", argsOnly = true)
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
