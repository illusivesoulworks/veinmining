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

package com.illusivesoulworks.veinmining;

import com.illusivesoulworks.veinmining.client.NeoForgeClientEventsListener;
import com.illusivesoulworks.veinmining.common.NeoForgeCommonEventsListener;
import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.network.CPacketState;
import com.illusivesoulworks.veinmining.common.network.SPacketNotify;
import com.illusivesoulworks.veinmining.common.network.VeinMiningClientPayloadHandler;
import com.illusivesoulworks.veinmining.common.network.VeinMiningServerPayloadHandler;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@Mod(VeinMiningConstants.MOD_ID)
public class VeinMiningNeoForgeMod {

  public VeinMiningNeoForgeMod(IEventBus eventBus) {
    VeinMiningConfig.setup();
    eventBus.addListener(this::setup);
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::registerPayloadHandler);
  }

  private void registerPayloadHandler(final RegisterPayloadHandlersEvent evt) {
    evt.registrar(VeinMiningConstants.MOD_ID)
        .playToServer(CPacketState.TYPE, CPacketState.STREAM_CODEC,
            VeinMiningServerPayloadHandler.getInstance()::handleState);
    evt.registrar(VeinMiningConstants.MOD_ID)
        .playToClient(SPacketNotify.TYPE, SPacketNotify.STREAM_CODEC,
            VeinMiningClientPayloadHandler.getInstance()::handleNotify);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    NeoForge.EVENT_BUS.register(new NeoForgeCommonEventsListener());
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    NeoForge.EVENT_BUS.register(new NeoForgeClientEventsListener());
  }

  @EventBusSubscriber(modid = VeinMiningConstants.MOD_ID, value = Dist.CLIENT)
  public static class ClientModEvents {

    @SubscribeEvent
    public static void registerKeys(final RegisterKeyMappingsEvent evt) {
      VeinMiningKey.setup();
      evt.registerCategory(VeinMiningKey.CATEGORY);
      evt.register(VeinMiningKey.get());
    }
  }
}
