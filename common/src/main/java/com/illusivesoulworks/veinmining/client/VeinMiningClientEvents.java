/*
 * Copyright (C) 2020-2022 Illusive Soulworks
 *
 * Vein Mining is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Vein Mining.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.veinmining.client;

import com.illusivesoulworks.veinmining.VeinMiningConstants;
import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.platform.ClientServices;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningKey;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class VeinMiningClientEvents {

  public static void tick() {
    Minecraft mc = Minecraft.getInstance();
    ClientLevel world = mc.level;
    LocalPlayer player = mc.player;

    if (world != null && player != null) {

      if (world.getGameTime() % 5 == 0) {
        boolean enabled;
        boolean needsEnchantment = VeinMiningConfig.SERVER.maxBlocksBase.get() == 0;
        VeinMiningConfig.ActivationState activationState =
            needsEnchantment ? VeinMiningConfig.CLIENT.activationState.get() :
                VeinMiningConfig.CLIENT.activationStateWithoutEnchantment.get();
        boolean isKeyDown = VeinMiningKey.get().isDown();

        if (VeinMiningConfig.CLIENT.enableEnchantmentWarnings.get() && isKeyDown) {
          Holder<Enchantment> enchantment = player.registryAccess().lookup(Registries.ENCHANTMENT)
              .map(enchantmentRegistryLookup -> enchantmentRegistryLookup.getOrThrow(
                  VeinMiningConstants.ENCHANTMENT)).orElse(null);
          int level = enchantment != null ?
              EnchantmentHelper.getItemEnchantmentLevel(enchantment, player.getMainHandItem()) : 0;

          if (activationState != VeinMiningConfig.ActivationState.HOLD_KEY_DOWN) {
            player.sendOverlayMessage(
                Component.translatable("tutorial.veinmining.key.no_configuration"));
          } else if (needsEnchantment && level == 0) {
            player.sendOverlayMessage(
                Component.translatable("tutorial.veinmining.key.no_enchantment"));
          }
        }

        if (activationState == VeinMiningConfig.ActivationState.STANDING) {
          enabled = !player.isCrouching();
        } else if (activationState == VeinMiningConfig.ActivationState.CROUCHING) {
          enabled = player.isCrouching();
        } else {
          enabled = isKeyDown;
        }
        ClientServices.PLATFORM.sendC2SState(enabled);
      }
    }
  }

  public static void tooltip(ItemStack stack, List<Component> tooltip) {
    boolean needsEnchantment = VeinMiningConfig.SERVER.maxBlocksBase.get() == 0;
    Minecraft mc = Minecraft.getInstance();
    LocalPlayer player = mc.player;

    if (player != null && needsEnchantment && !stack.is(Items.ENCHANTED_BOOK)) {

      if (VeinMiningConfig.CLIENT.enableEnchantmentTooltips.get()) {
        Holder<Enchantment> enchantment = player.registryAccess().lookup(Registries.ENCHANTMENT)
            .map(enchantmentRegistryLookup -> enchantmentRegistryLookup.getOrThrow(
                VeinMiningConstants.ENCHANTMENT)).orElse(null);
        int level = enchantment != null ?
            EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack) : 0;

        if (level > 0) {
          MutableComponent component = getTutorialMessage();
          component.withStyle(ChatFormatting.DARK_AQUA);
          int index = tooltip.size();

          for (int i = 0; i < tooltip.size(); i++) {
            Component component1 = tooltip.get(i);

            if (component1 instanceof MutableComponent mutableComponent &&
                mutableComponent.getContents() instanceof TranslatableContents contents1) {

              if (contents1.getKey().startsWith("item.modifiers.")) {
                index = i;
                break;
              }

              if (contents1.getKey().startsWith("enchantments.")) {
                index = i;
                break;
              }
            }
          }
          tooltip.add(index, component);
          tooltip.add(index + 1, Component.empty());
        }
      }
    }
  }

  public static MutableComponent getTutorialMessage() {
    MutableComponent component = null;

    switch (VeinMiningConfig.CLIENT.activationState.get()) {
      case STANDING ->
          component = Component.translatable("tutorial.veinmining.enchantment.standing");
      case CROUCHING ->
          component = Component.translatable("tutorial.veinmining.enchantment.crouching");
      case HOLD_KEY_DOWN -> {
        if (VeinMiningKey.get().isUnbound()) {
          component = Component.translatable("tutorial.veinmining.enchantment.no_key")
              .withStyle(ChatFormatting.RED);
        } else {
          Component name = VeinMiningKey.get().getTranslatedKeyMessage();

          if (name instanceof MutableComponent mutableComponent) {
            mutableComponent.withStyle(ChatFormatting.YELLOW);
          }
          component = Component.translatable("tutorial.veinmining.enchantment.key", name);
        }
      }
    }
    return component;
  }
}
