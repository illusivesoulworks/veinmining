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
import com.illusivesoulworks.veinmining.common.network.StatePayload;
import com.illusivesoulworks.veinmining.common.network.VeinMiningServerPayloadHandler;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningKey;
import com.illusivesoulworks.veinmining.common.veinmining.enchantment.VeinMiningEnchantment;
import java.util.function.Supplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(VeinMiningConstants.MOD_ID)
public class VeinMiningNeoForgeMod {

  private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(
      BuiltInRegistries.ENCHANTMENT, VeinMiningConstants.MOD_ID);
  public static final Supplier<Enchantment> ENCHANTMENT =
      ENCHANTMENTS.register(VeinMiningConstants.ENCHANTMENT_ID.getPath(),
          VeinMiningEnchantment::new);

  public VeinMiningNeoForgeMod(IEventBus eventBus) {
    VeinMiningMod.init();
    ENCHANTMENTS.register(eventBus);
    eventBus.addListener(this::setup);
    eventBus.addListener(this::clientSetup);
    eventBus.addListener(this::registerPayloadHandler);
  }

  private void registerPayloadHandler(final RegisterPayloadHandlerEvent evt) {
    evt.registrar(VeinMiningConstants.MOD_ID).play(StatePayload.ID, StatePayload::new,
        handler -> handler.server(VeinMiningServerPayloadHandler.getInstance()::handleState));
  }

  private void setup(final FMLCommonSetupEvent evt) {
    NeoForge.EVENT_BUS.register(new NeoForgeCommonEventsListener());
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    NeoForge.EVENT_BUS.register(new NeoForgeClientEventsListener());
  }

  @Mod.EventBusSubscriber(modid = VeinMiningConstants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class ClientModEvents {

    @SubscribeEvent
    public static void registerKeys(final RegisterKeyMappingsEvent evt) {
      VeinMiningKey.setup();
      evt.register(VeinMiningKey.get());
    }
  }
}
