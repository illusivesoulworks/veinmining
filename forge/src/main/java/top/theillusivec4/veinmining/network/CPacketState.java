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

import java.util.function.Supplier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;

public class CPacketState {

  private final boolean activate;

  public CPacketState(boolean activate) {
    this.activate = activate;
  }

  public static void send(boolean activate) {
    VeinMiningNetwork.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketState(activate));
  }

  public static void encode(CPacketState msg, PacketBuffer buf) {
    buf.writeBoolean(msg.activate);
  }

  public static CPacketState decode(PacketBuffer buf) {
    return new CPacketState(buf.readBoolean());
  }

  public static void handle(CPacketState msg, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity sender = ctx.get().getSender();

      if (sender != null) {

        if (msg.activate) {
          VeinMiningPlayers.startVeinMining(sender, sender.world.getGameTime());
        } else {
          VeinMiningPlayers.stopVeinMining(sender);
        }
      }
    });
    ctx.get().setPacketHandled(true);
  }
}
