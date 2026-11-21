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

package com.illusivesoulworks.veinmining.common.network;

import com.illusivesoulworks.veinmining.VeinMiningConstants;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningPlayers;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

public record CPacketState(boolean activate) implements CustomPacketPayload {

  public static final Type<CPacketState> TYPE =
      new Type<>(Identifier.fromNamespaceAndPath(VeinMiningConstants.MOD_ID, "state"));
  public static final StreamCodec<FriendlyByteBuf, CPacketState> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.BOOL,
          CPacketState::activate,
          CPacketState::new);

  public static void handle(boolean activate, ServerPlayer player) {

    if (activate) {
      VeinMiningPlayers.activateVeinMining(player, player.level().getGameTime());
    } else {
      VeinMiningPlayers.deactivateVeinMining(player);
    }
  }

  @Nonnull
  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
