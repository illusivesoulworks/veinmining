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

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import top.theillusivec4.veinmining.VeinMiningMod;

public class VeinMiningNetwork {

  private static final String PTC_VERSION = "1";

  private static SimpleChannel instance;
  private static int id = 0;

  public static void register() {
    instance =
        NetworkRegistry.ChannelBuilder.named(new ResourceLocation(VeinMiningMod.MOD_ID, "main"))
            .networkProtocolVersion(() -> PTC_VERSION).clientAcceptedVersions(PTC_VERSION::equals)
            .serverAcceptedVersions(PTC_VERSION::equals).simpleChannel();
    registerMessage(CPacketState.class, CPacketState::encode, CPacketState::decode,
        CPacketState::handle);
  }

  private static <M> void registerMessage(Class<M> messageType,
                                          BiConsumer<M, FriendlyByteBuf> encoder,
                                          Function<FriendlyByteBuf, M> decoder,
                                          BiConsumer<M, Supplier<NetworkEvent.Context>> messageConsumer) {
    instance.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
  }

  public static void sendC2SState(boolean activate) {
    VeinMiningNetwork.instance.send(PacketDistributor.SERVER.noArg(), new CPacketState(activate));
  }
}
