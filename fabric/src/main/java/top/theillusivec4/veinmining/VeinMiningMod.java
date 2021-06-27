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

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.config.VeinMiningConfigData;
import top.theillusivec4.veinmining.network.VeinMiningNetwork;
import top.theillusivec4.veinmining.veinmining.VeinMiningEnchantment;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;
import top.theillusivec4.veinmining.veinmining.logic.BlockProcessor;

public class VeinMiningMod implements ModInitializer {

  public static final String MOD_ID = "veinmining";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final Enchantment VEIN_MINING = new VeinMiningEnchantment();
  public static VeinMiningConfigData configData;

  @Override
  public void onInitialize() {
    Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "vein_mining"), VEIN_MINING);
    configData =
        AutoConfig.register(VeinMiningConfigData.class, JanksonConfigSerializer::new).getConfig();
    VeinMiningConfig.bake(configData);
    AutoConfig.getConfigHolder(VeinMiningConfigData.class)
        .registerSaveListener((configHolder, veinMiningConfigData) -> {
          VeinMiningConfig.bake(configData);
          return ActionResult.PASS;
        });
    ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
      VeinMiningConfig.bake(configData);
      BlockProcessor.rebuild();
    });
    ServerLifecycleEvents.END_DATA_PACK_RELOAD
        .register((minecraftServer, serverResourceManager, b) -> BlockProcessor.rebuild());
    ServerTickEvents.END_WORLD_TICK
        .register((world) -> VeinMiningPlayers.validate(world.getTime()));
    ServerPlayNetworking
        .registerGlobalReceiver(VeinMiningNetwork.SEND_STATE, VeinMiningNetwork::handleState);
  }
}
