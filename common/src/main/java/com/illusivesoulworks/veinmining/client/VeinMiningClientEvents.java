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

import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.platform.ClientServices;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;

public class VeinMiningClientEvents {

  public static void tick() {
    Minecraft mc = Minecraft.getInstance();
    ClientLevel world = mc.level;
    LocalPlayer player = mc.player;

    if (world != null && player != null) {

      if (world.getGameTime() % 5 == 0) {
        boolean enabled;
        VeinMiningConfig.ActivationState activationState =
            VeinMiningConfig.SERVER.maxBlocksBase.get() > 0 &&
                VeinMiningConfig.SERVER.maxDistanceBase.get() > 0 ?
                VeinMiningConfig.CLIENT.activationStateWithoutEnchantment.get() :
                VeinMiningConfig.CLIENT.activationState.get();

        if (activationState == VeinMiningConfig.ActivationState.STANDING) {
          enabled = !player.isCrouching();
        } else if (activationState == VeinMiningConfig.ActivationState.CROUCHING) {
          enabled = player.isCrouching();
        } else {
          enabled = VeinMiningKey.get().isDown();
        }
        ClientServices.PLATFORM.sendC2SState(enabled);
      }
    }
  }
}
