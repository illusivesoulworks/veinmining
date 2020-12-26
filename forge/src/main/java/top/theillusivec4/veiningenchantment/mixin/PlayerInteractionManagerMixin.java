package top.theillusivec4.veiningenchantment.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.veiningenchantment.veining.VeiningManager;

@Mixin(PlayerInteractionManager.class)
public class PlayerInteractionManagerMixin {

  @Shadow
  public ServerPlayerEntity player;

  private Block source = Blocks.AIR;

  @Inject(
      at = @At(value = "HEAD"),
      method = "tryHarvestBlock(Lnet/minecraft/util/math/BlockPos;)Z")
  private void _veiningenchantment_preHarvest(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
    source = player.world.getBlockState(pos).getBlock();
  }

  @Inject(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/block/Block.harvestBlock(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/item/ItemStack;)V"),
      method = "tryHarvestBlock(Lnet/minecraft/util/math/BlockPos;)Z")
  private void _veiningenchantment_tryHarvest(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
    VeiningManager.veining(player, pos, source);
  }
}
