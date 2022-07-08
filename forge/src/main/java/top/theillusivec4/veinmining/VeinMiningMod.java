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

package top.theillusivec4.veinmining;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
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

@Mod.EventBusSubscriber(modid = VeinMiningMod.MOD_ID, bus = Bus.MOD)
@Mod(VeinMiningMod.MOD_ID)
public class VeinMiningMod {

  public static final String MOD_ID = "veinmining";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final Enchantment VEIN_MINING = new VeinMiningEnchantment();
  public static final DeferredRegister<Enchantment> ENCHANTMENTS =
    DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, VeinMiningMod.MOD_ID);

  public static RegistryObject<Enchantment> VEIN_MINING_OBJECT =
    ENCHANTMENTS.register("vein_mining", () -> VEIN_MINING);

  static {
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, VeinMiningConfig.CONFIG_SPEC);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientVeinMiningConfig.CONFIG_SPEC);
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ENCHANTMENTS.register(eventBus);
  }

  @SubscribeEvent
  public static void commonSetup(final FMLCommonSetupEvent evt) {
    VeinMiningNetwork.register();
    MinecraftForge.EVENT_BUS.register(new VeinMiningEventsListener());
  }

  @SubscribeEvent
  public static void clientSetup(final FMLClientSetupEvent evt) {
    ClientRegistry.registerKeyBinding(VeinMiningKey.get());
    MinecraftForge.EVENT_BUS.register(new VeinMiningClientEventsListener());
  }

  @SubscribeEvent
  public static void configLoading(final ModConfigEvent.Loading evt) {
    bakeConfigs(evt);
  }

  @SubscribeEvent
  public static void configReloading(final ModConfigEvent.Reloading evt) {
    bakeConfigs(evt);
  }

  private static void bakeConfigs(final ModConfigEvent evt) {
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
