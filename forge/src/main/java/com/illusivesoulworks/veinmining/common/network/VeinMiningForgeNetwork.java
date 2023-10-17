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
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class VeinMiningForgeNetwork {

  private static final int PTC_VERSION = 1;

  public static SimpleChannel instance;

  public static SimpleChannel get() {
    return instance;
  }

  public static void setup() {
    instance = ChannelBuilder.named(new ResourceLocation(VeinMiningConstants.MOD_ID, "main"))
        .networkProtocolVersion(PTC_VERSION)
        .clientAcceptedVersions(Channel.VersionTest.exact(PTC_VERSION))
        .serverAcceptedVersions(Channel.VersionTest.exact(PTC_VERSION)).simpleChannel();

    // Client-to-Server
    registerC2S(CPacketState.class, CPacketState::encode, CPacketState::decode,
        CPacketState::handle);
  }

  public static <M> void registerC2S(Class<M> messageType, BiConsumer<M, FriendlyByteBuf> encoder,
                                     Function<FriendlyByteBuf, M> decoder,
                                     BiConsumer<M, ServerPlayer> messageConsumer) {
    instance.messageBuilder(messageType).decoder(decoder).encoder(encoder)
        .consumerNetworkThread((m, context) -> {
          context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();

            if (sender != null) {
              messageConsumer.accept(m, sender);
            }
          });
          context.setPacketHandled(true);
        }).add();
  }
}
