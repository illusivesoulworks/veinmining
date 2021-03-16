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

package top.theillusivec4.veinmining;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.network.VeinMiningNetwork;
import top.theillusivec4.veinmining.veinmining.VeinMiningKey;

public class ClientVeinMiningMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    KeyBindingHelper.registerKeyBinding(VeinMiningKey.get());
    ClientTickEvents.END_CLIENT_TICK.register((client) -> {
      ClientWorld world = client.world;
      ClientPlayerEntity player = client.player;

      if (world != null && player != null && world.getTime() % 5 == 0) {
        boolean enabled;

        if (VeinMiningConfig.VeinMining.activationState == VeinMiningConfig.ActivationState.STANDING) {
          enabled = !player.isSneaking();
        } else if (VeinMiningConfig.VeinMining.activationState == VeinMiningConfig.ActivationState.CROUCHING) {
          enabled = player.isSneaking();
        } else {
          enabled = VeinMiningKey.get().isPressed();
        }
        VeinMiningNetwork.sendState(enabled);
      }
    });
  }
}
