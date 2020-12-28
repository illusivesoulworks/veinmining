package top.theillusivec4.veinmining.mixin;

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
import top.theillusivec4.veinmining.veinmining.logic.VeinMiningLogic;

@Mixin(PlayerInteractionManager.class)
public class PlayerInteractionManagerMixin {

  @Shadow
  public ServerPlayerEntity player;

  private Block source = Blocks.AIR;

  @Inject(
      at = @At(value = "HEAD"),
      method = "tryHarvestBlock(Lnet/minecraft/util/math/BlockPos;)Z")
  private void _veinmining_preHarvest(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
    source = player.world.getBlockState(pos).getBlock();
  }

  @Inject(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/item/ItemStack.onBlockDestroyed(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V"),
      method = "tryHarvestBlock(Lnet/minecraft/util/math/BlockPos;)Z")
  private void _veinmining_tryHarvest(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
    VeinMiningLogic.startVeinMining(player, pos, source);
  }
}
