/*
 * Copyright (c) 2020 C4
 *
 * This file is part of Vein Mining, a mod made for Minecraft.
 *
 * Vein Mining is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Vein Mining.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.veinmining;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.veinmining.config.ClientVeinMiningConfig;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.network.VeinMiningNetwork;
import top.theillusivec4.veinmining.veinmining.VeinMiningEnchantment;
import top.theillusivec4.veinmining.veinmining.VeinMiningKey;
import top.theillusivec4.veinmining.veinmining.event.VeinMiningClientEventsListener;
import top.theillusivec4.veinmining.veinmining.event.VeinMiningEventsListener;
import top.theillusivec4.veinmining.veinmining.logic.BlockProcessor;

@Mod(VeinMiningMod.MOD_ID)
public class VeinMiningMod {

  public static final String MOD_ID = "veinmining";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final Enchantment VEIN_MINING = new VeinMiningEnchantment();

  public VeinMiningMod() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addGenericListener(Enchantment.class, this::registerEnchantment);
    eventBus.addListener(this::configLoading);
    eventBus.addListener(this::configReloading);
    eventBus.addListener(this::commonSetup);
    eventBus.addListener(this::clientSetup);
    ModLoadingContext.get()
        .registerConfig(ModConfig.Type.SERVER, VeinMiningConfig.CONFIG_SPEC);
    ModLoadingContext.get()
        .registerConfig(ModConfig.Type.CLIENT, ClientVeinMiningConfig.CONFIG_SPEC);
  }

  private void commonSetup(final FMLCommonSetupEvent evt) {
    VeinMiningNetwork.register();
    MinecraftForge.EVENT_BUS.register(new VeinMiningEventsListener());
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    ClientRegistry.registerKeyBinding(VeinMiningKey.get());
    MinecraftForge.EVENT_BUS.register(new VeinMiningClientEventsListener());
  }

  private void registerEnchantment(final RegistryEvent.Register<Enchantment> evt) {
    evt.getRegistry().register(VEIN_MINING.setRegistryName(VeinMiningEnchantment.ID));
  }

  private void configLoading(final ModConfig.Loading evt) {
    this.bakeConfigs(evt);
  }

  private void configReloading(final ModConfig.Reloading evt) {
    this.bakeConfigs(evt);
  }

  private void bakeConfigs(final ModConfig.ModConfigEvent evt) {
    ModConfig config = evt.getConfig();

    if (config.getModId().equals(MOD_ID)) {

      if (config.getType() == ModConfig.Type.CLIENT) {
        ClientVeinMiningConfig.bake();
      } else {
        VeinMiningConfig.bake();
        BlockProcessor.rebuild();
      }
    }
  }
}
