package top.theillusivec4.veinmining.mixin.core;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.theillusivec4.veinmining.mixin.VeinMiningMixinHooks;

@Mixin(Block.class)
public class BlockMixin {

  @ModifyVariable(at = @At("HEAD"), method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V", argsOnly = true)
  private static BlockPos veinmining$dropStack(BlockPos pos, World level, BlockPos unused,
                                                 ItemStack stack) {
    return VeinMiningMixinHooks.getActualSpawnPos(level, pos);
  }

  @ModifyVariable(at = @At("HEAD"), method = "dropStack(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/item/ItemStack;)V", argsOnly = true)
  private static BlockPos veinmining$dropStackFromFace(BlockPos pos, World level, BlockPos unused,
                                                         Direction direction, ItemStack stack) {
    return VeinMiningMixinHooks.getActualSpawnPos(level, pos);
  }
}
