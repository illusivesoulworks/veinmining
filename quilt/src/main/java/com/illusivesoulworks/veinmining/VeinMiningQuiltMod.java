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
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;
import org.quiltmc.qsl.lifecycle.api.event.ServerWorldTickEvents;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;

public class VeinMiningQuiltMod implements ModInitializer {

  public static final ResourceLocation STATE_PACKET =
      new ResourceLocation(VeinMiningConstants.MOD_ID, "state");

  @Override
  public void onInitialize(ModContainer modContainer) {
    Registry.register(BuiltInRegistries.ENCHANTMENT, VeinMiningConstants.ENCHANTMENT_ID,
        VeinMiningMod.ENCHANTMENT);
    ServerLifecycleEvents.READY.register(server -> VeinMiningEvents.reloadDatapack());
    ResourceLoaderEvents.END_DATA_PACK_RELOAD.register(
        context -> VeinMiningEvents.reloadDatapack());
    ServerWorldTickEvents.END.register((server, world) -> VeinMiningEvents.tick(world));
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
