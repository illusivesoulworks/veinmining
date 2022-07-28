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

package top.theillusivec4.veinmining.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;

public class CPacketState {

  private final boolean activate;

  public CPacketState(boolean activate) {
    this.activate = activate;
  }

  public static void encode(CPacketState msg, FriendlyByteBuf buf) {
    buf.writeBoolean(msg.activate);
  }

  public static CPacketState decode(FriendlyByteBuf buf) {
    return new CPacketState(buf.readBoolean());
  }

  public static void handle(CPacketState msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayer sender = ctx.get().getSender();

      if (sender != null) {

        if (msg.activate) {
          VeinMiningPlayers.activateVeinMining(sender, sender.level.getGameTime());
        } else {
          VeinMiningPlayers.deactivateVeinMining(sender);
        }
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
