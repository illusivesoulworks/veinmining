package top.theillusivec4.veinmining.mixin.core;

import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.veinmining.mixin.VeinMiningMixinHooks;

@Mixin(ItemStack.class)
public class ItemStackMixin {

  @Inject(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/world/item/ItemStack.isDamageableItem()Z"),
      method = "hurtAndBreak",
      cancellable = true)
  private <T extends LivingEntity> void veinmining$hurtAndBreak(int amount, T entity,
                                                                Consumer<T> consumer,
                                                                CallbackInfo ci) {

    if (VeinMiningMixinHooks.shouldCancelItemDamage(entity)) {
      ci.cancel();
    }
  }

  @SuppressWarnings("ConstantConditions")
  @ModifyArg(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/world/item/ItemStack.hurt(ILnet/minecraft/util/RandomSource;Lnet/minecraft/server/level/ServerPlayer;)Z"),
      method = "hurtAndBreak")
  private <T extends LivingEntity> int veinmining$changeBreak(int amount, RandomSource randomSource,
                                                              ServerPlayer player) {
    return VeinMiningMixinHooks.modifyItemDamage((ItemStack) (Object) this, amount, player);
  }
}
