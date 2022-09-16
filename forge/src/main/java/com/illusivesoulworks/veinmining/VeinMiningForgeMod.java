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

import com.illusivesoulworks.veinmining.client.ForgeClientEventsListener;
import com.illusivesoulworks.veinmining.common.ForgeCommonEventsListener;
import com.illusivesoulworks.veinmining.common.network.VeinMiningForgeNetwork;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(VeinMiningConstants.MOD_ID)
public class VeinMiningForgeMod {

  private static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(
      ForgeRegistries.ENCHANTMENTS, VeinMiningConstants.MOD_ID);
  private static final RegistryObject<Enchantment> ENCHANTMENT =
      ENCHANTMENTS.register(VeinMiningConstants.ENCHANTMENT_ID.getPath(),
          () -> VeinMiningMod.ENCHANTMENT);

  public VeinMiningForgeMod() {
    VeinMiningMod.init();
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ENCHANTMENTS.register(eventBus);
    eventBus.addListener(this::setup);
    eventBus.addListener(this::clientSetup);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    VeinMiningForgeNetwork.setup();
    MinecraftForge.EVENT_BUS.register(new ForgeCommonEventsListener());
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(new ForgeClientEventsListener());
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
