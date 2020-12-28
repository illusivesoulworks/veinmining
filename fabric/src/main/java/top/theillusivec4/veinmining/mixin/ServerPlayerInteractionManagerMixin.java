package top.theillusivec4.veinmining.mixin;

import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.veinmining.veinmining.logic.VeinMiningLogic;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {

  @Shadow
  public ServerPlayerEntity player;

  private Block source;

  @Inject(
      at = @At(value = "HEAD"),
      method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z")
  private void _veinmining_preHarvest(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
    source = player.world.getBlockState(pos).getBlock();
  }

  @Inject(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/item/ItemStack.postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V"),
      method = "tryBreakBlock(Lnet/minecraft/util/math/BlockPos;)Z")
  private void _veinmining_tryHarvest(BlockPos pos, CallbackInfoReturnable<Boolean> ci) {
    VeinMiningLogic.startVeinMining(player, pos, source);
  }
}
