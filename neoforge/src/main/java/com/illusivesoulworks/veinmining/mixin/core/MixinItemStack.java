package com.illusivesoulworks.veinmining.mixin.core;

import com.illusivesoulworks.veinmining.mixin.VeinMiningMixinHooks;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class MixinItemStack {

  @Inject(
      // 26.1 moved the isDamageableItem() check into processDurabilityChange, so cancel at HEAD
      at = @At("HEAD"),
      method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
      cancellable = true)
  private void veinmining$itemDamage(int amount, ServerLevel serverLevel,
                                     @Nullable LivingEntity livingEntity, Consumer<Item> consumer,
                                     CallbackInfo ci) {

    if (livingEntity instanceof ServerPlayer serverPlayer &&
        VeinMiningMixinHooks.shouldCancelItemDamage(serverPlayer)) {
      ci.cancel();
    }
  }

  @SuppressWarnings("ConstantConditions")
  @ModifyVariable(
      at = @At("HEAD"),
      method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V",
      argsOnly = true
  )
  private int veinmining$changeBreak(int amount, int unused, ServerLevel serverLevel,
                                     @Nullable LivingEntity livingEntity) {

    if (livingEntity instanceof ServerPlayer serverPlayer) {
      return VeinMiningMixinHooks.modifyItemDamage((ItemStack) (Object) this, amount, serverPlayer);
    }
    return amount;
  }
}
