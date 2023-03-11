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

package top.theillusivec4.veinmining.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;

public class VeinMiningNetwork {

  public static final Identifier SEND_STATE = new Identifier(VeinMiningMod.MOD_ID, "state");

  public static void sendState(boolean state) {
    PacketByteBuf buf = PacketByteBufs.create();
    buf.writeBoolean(state);
    ClientPlayNetworking.send(SEND_STATE, buf);
  }

  public static void handleState(MinecraftServer minecraftServer,
                                 ServerPlayerEntity serverPlayerEntity,
                                 ServerPlayNetworkHandler serverPlayNetworkHandler,
                                 PacketByteBuf packetByteBuf, PacketSender packetSender) {
    boolean flag = packetByteBuf.readBoolean();
    minecraftServer.execute(() -> {
      if (flag) {
        VeinMiningPlayers.activateVeinMining(serverPlayerEntity, serverPlayerEntity.world.getTime());
      } else {
        VeinMiningPlayers.deactivateVeinMining(serverPlayerEntity);
      }
    });
  }
}
