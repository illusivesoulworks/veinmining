package com.illusivesoulworks.veinmining.common.network;

import com.illusivesoulworks.veinmining.client.VeinMiningClientEvents;
import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import net.minecraft.client.Minecraft;

public class ClientPackets {

  public static void handleNotification() {
    Minecraft mc = Minecraft.getInstance();

    if (mc.player != null && VeinMiningConfig.CLIENT.enableEnchantmentNotifications.get()) {
      mc.player.sendOverlayMessage(VeinMiningClientEvents.getTutorialMessage());
    }
  }
}
