/*
 * Copyright (c) 2021 C4
 *
 * This file is part of Vein Mining, a mod made for Minecraft.
 *
 * Vein Mining is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Vein Mining.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.veinmining.veinmining.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.veinmining.config.ClientVeinMiningConfig;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.network.CPacketState;
import top.theillusivec4.veinmining.veinmining.VeinMiningKey;

public class VeinMiningClientEventsListener {

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void veinMiningState(final TickEvent.ClientTickEvent evt) {

    if (evt.phase == TickEvent.Phase.END) {
      Minecraft mc = Minecraft.getInstance();
      ClientWorld world = mc.world;
      ClientPlayerEntity player = mc.player;

      if (world != null && player != null) {

        if (world.getGameTime() % 5 == 0) {
          boolean enabled;
          VeinMiningConfig.ActivationState activationState =
              VeinMiningConfig.VeinMining.maxBlocksBase > 0 &&
                  VeinMiningConfig.VeinMining.maxDistanceBase > 0 ?
                  ClientVeinMiningConfig.activationStateWithoutEnchantment :
                  ClientVeinMiningConfig.activationState;

          if (activationState == VeinMiningConfig.ActivationState.STANDING) {
            enabled = !player.isCrouching();
          } else if (activationState == VeinMiningConfig.ActivationState.CROUCHING) {
            enabled = player.isCrouching();
          } else {
            enabled = VeinMiningKey.get().isKeyDown();
          }
          CPacketState.send(enabled);
        }
      }
    }
  }
}
