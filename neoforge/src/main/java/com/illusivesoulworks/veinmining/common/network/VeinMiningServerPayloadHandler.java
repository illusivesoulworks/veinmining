package com.illusivesoulworks.veinmining.common.network;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class VeinMiningServerPayloadHandler {

  private static final VeinMiningServerPayloadHandler INSTANCE =
      new VeinMiningServerPayloadHandler();

  public static VeinMiningServerPayloadHandler getInstance() {
    return INSTANCE;
  }

  public void handleState(final StatePayload packet, final PlayPayloadContext ctx) {
    ctx.workHandler().submitAsync(() -> ctx.player().ifPresent(player -> {

          if (player instanceof ServerPlayer serverPlayer) {
            CPacketState.handle(packet.activate(), serverPlayer);
          }
        }))
        .exceptionally(e -> {
          ctx.packetHandler()
              .disconnect(Component.translatable("veinmining.networking.failed", e.getMessage()));
          return null;
        });
  }
}
