package top.theillusivec4.veinmining.mixin.core;

import java.util.Random;
import java.util.function.Consumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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
          target = "net/minecraft/item/ItemStack.isDamageable()Z"),
      method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
      cancellable = true)
  private <T extends LivingEntity> void veinmining$damage(int amount, T entity,
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
          target = "net/minecraft/item/ItemStack.damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z"),
      method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V")
  private <T extends LivingEntity> int veinmining$changeDamage(int amount, Random randomSource,
                                                               ServerPlayerEntity player) {
    return VeinMiningMixinHooks.modifyItemDamage((ItemStack) (Object) this, amount, player);
  }
}
