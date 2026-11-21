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

import com.illusivesoulworks.veinmining.common.data.BlockGroupsResourceListener;
import com.illusivesoulworks.veinmining.common.network.CPacketState;
import com.illusivesoulworks.veinmining.common.network.SPacketNotify;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;

public class VeinMiningFabricMod implements ModInitializer {

  @Override
  public void onInitialize() {
    ServerLifecycleEvents.SERVER_STARTED.register(server -> VeinMiningEvents.reloadDatapack());
    ServerTickEvents.END_LEVEL_TICK.register(VeinMiningEvents::tick);
    BlockGroupsResourceListener.INSTANCE = new BlockGroupsResourceListener();
    ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(
        Identifier.fromNamespaceAndPath(VeinMiningConstants.MOD_ID, "groups"),
        BlockGroupsResourceListener.INSTANCE);
    ServerPlayConnectionEvents.DISCONNECT.register(
        (handler, server) -> VeinMiningEvents.playerLoggedOut(handler.getPlayer()));
    PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
      if (player instanceof ServerPlayer serverPlayer) {
        VeinMiningEvents.blockBreak(serverPlayer, pos, state);
      }
    });
    PayloadTypeRegistry.serverboundPlay().register(CPacketState.TYPE, CPacketState.STREAM_CODEC);
    PayloadTypeRegistry.clientboundPlay().register(SPacketNotify.TYPE, SPacketNotify.STREAM_CODEC);
    ServerPlayNetworking.registerGlobalReceiver(CPacketState.TYPE, (payload, context) -> {
      ServerPlayer player = context.player();
      MinecraftServer server = player.level().getServer();
      boolean activate = payload.activate();

      if (server != null) {
        server.execute(() -> CPacketState.handle(activate, player));
      }
    });
    ServerEntityEvents.EQUIPMENT_CHANGE.register(
        (livingEntity, equipmentSlot, previousStack, currentStack) -> VeinMiningEvents.toolEquip(
            currentStack, previousStack, equipmentSlot, livingEntity));
  }
}
