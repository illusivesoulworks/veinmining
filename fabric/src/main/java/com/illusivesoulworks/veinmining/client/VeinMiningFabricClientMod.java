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

import com.illusivesoulworks.veinmining.common.network.SPacketNotify;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningKey;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class VeinMiningFabricClientMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    VeinMiningKey.setup();
    KeyMappingHelper.registerKeyMapping(VeinMiningKey.get());
    ClientTickEvents.END_CLIENT_TICK.register(client -> VeinMiningClientEvents.tick());
    ItemTooltipCallback.EVENT.register(
        (stack, player, context, lines) -> VeinMiningClientEvents.tooltip(stack, lines));
    ClientPlayNetworking.registerGlobalReceiver(SPacketNotify.TYPE,
        (payload, context) -> context.client().execute(SPacketNotify::handle));
  }
}
