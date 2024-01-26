package com.illusivesoulworks.veinmining.common.network;

import com.illusivesoulworks.veinmining.VeinMiningConstants;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record StatePayload(boolean activate) implements CustomPacketPayload {

  public static final ResourceLocation ID =
      new ResourceLocation(VeinMiningConstants.MOD_ID, "state");

  public StatePayload(FriendlyByteBuf buf) {
    this(buf.readBoolean());
  }

  @Override
  public void write(@Nonnull FriendlyByteBuf buf) {
    buf.writeBoolean(this.activate());
  }

  @Nonnull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
