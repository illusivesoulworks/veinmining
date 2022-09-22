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

package com.illusivesoulworks.veinmining;

import com.illusivesoulworks.veinmining.common.network.CPacketState;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningEvents;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningPlayers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class VeinMiningFabricMod implements ModInitializer {

  public static final ResourceLocation STATE_PACKET =
      new ResourceLocation(VeinMiningConstants.MOD_ID, "state");

  @Override
  public void onInitialize() {
    Registry.register(Registry.ENCHANTMENT, VeinMiningConstants.ENCHANTMENT_ID,
        VeinMiningMod.ENCHANTMENT);
    ServerLifecycleEvents.SERVER_STARTED.register(server -> VeinMiningEvents.reloadDatapack());
    ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(
        (server, resourceManager, success) -> VeinMiningEvents.reloadDatapack());
    ServerTickEvents.END_WORLD_TICK.register(
        world -> VeinMiningPlayers.validate(world.getGameTime()));
    PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
      if (player instanceof ServerPlayer serverPlayer) {
        VeinMiningEvents.blockBreak(serverPlayer, pos, state);
      }
    });
    ServerPlayNetworking.registerGlobalReceiver(STATE_PACKET,
        (server, player, handler, buf, responseSender) -> {
          CPacketState msg = CPacketState.decode(buf);
          server.execute(() -> CPacketState.handle(msg, player));
        });
  }
}
