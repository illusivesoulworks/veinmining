package com.illusivesoulworks.veinmining.common.network;

import com.illusivesoulworks.veinmining.VeinMiningConstants;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SPacketNotify() implements CustomPacketPayload {

  public static final Type<SPacketNotify> TYPE =
      new Type<>(Identifier.fromNamespaceAndPath(VeinMiningConstants.MOD_ID, "notify"));
  public static final SPacketNotify INSTANCE = new SPacketNotify();
  public static final StreamCodec<FriendlyByteBuf, SPacketNotify> STREAM_CODEC =
      StreamCodec.unit(INSTANCE);

  public static void handle() {
    ClientPackets.handleNotification();
  }

  @Nonnull
  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
