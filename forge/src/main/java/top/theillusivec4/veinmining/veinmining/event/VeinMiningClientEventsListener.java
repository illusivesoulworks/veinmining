/*
 * Copyright (C) 2020-2021 C4
 *
 * This file is part of Vein Mining.
 *
 * Vein Mining is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Vein Mining.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package top.theillusivec4.veinmining.veinmining.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.veinmining.config.ClientVeinMiningConfig;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.network.VeinMiningNetwork;
import top.theillusivec4.veinmining.veinmining.VeinMiningKey;

public class VeinMiningClientEventsListener {

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void veinMiningState(final TickEvent.ClientTickEvent evt) {

    if (evt.phase == TickEvent.Phase.END) {
      Minecraft mc = Minecraft.getInstance();
      ClientLevel world = mc.level;
      LocalPlayer player = mc.player;

      if (world != null && player != null) {

        if (world.getGameTime() % 5 == 0) {
          boolean enabled;

          if (ClientVeinMiningConfig.activationState == VeinMiningConfig.ActivationState.STANDING) {
            enabled = !player.isCrouching();
          } else if (ClientVeinMiningConfig.activationState ==
              VeinMiningConfig.ActivationState.CROUCHING) {
            enabled = player.isCrouching();
          } else {
            enabled = VeinMiningKey.get().isDown();
          }
          VeinMiningNetwork.sendC2SState(enabled);
        }
      }
    }
  }
}
