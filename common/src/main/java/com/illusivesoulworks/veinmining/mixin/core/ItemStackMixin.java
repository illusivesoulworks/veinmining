package com.illusivesoulworks.veinmining.mixin.core;

import com.illusivesoulworks.veinmining.mixin.VeinMiningMixinHooks;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

  @Inject(
      at = @At(
          value = "INVOKE",
          target = "net/minecraft/world/item/ItemStack.isDamageableItem()Z"),
      method = "hurtAndBreak")
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
