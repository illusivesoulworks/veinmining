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

package top.theillusivec4.veinmining.veinmining.event;

import javax.annotation.Nonnull;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import top.theillusivec4.veinmining.capabilities.IVeinCapability;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;
import top.theillusivec4.veinmining.veinmining.logic.BlockProcessor;
import top.theillusivec4.veinmining.veinmining.logic.VeinMiningLogic;

public class VeinMiningEventsListener {
	
  @SubscribeEvent
  @SuppressWarnings("unused")
  public void worldTick(final TickEvent.WorldTickEvent evt) {

    if (!evt.world.isClientSide()) {
      VeinMiningPlayers.validate(evt.world.getGameTime());
    }
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void reload(final AddReloadListenerEvent evt) {
    evt.addListener(new SimplePreparableReloadListener<Void>() {

      @Nonnull
      @Override
      protected Void prepare(@Nonnull ResourceManager resourceManagerIn,
                             @Nonnull ProfilerFiller profilerIn) {
        return null;
      }

      @Override
      protected void apply(@Nonnull Void objectIn, @Nonnull ResourceManager resourceManagerIn,
                           @Nonnull ProfilerFiller profilerIn) {
        BlockProcessor.rebuild();
      }
    });
  }
  
  @EventBusSubscriber
  class BreakListener {
	  
	  @SubscribeEvent
	  static void blockBreak(BreakEvent evt) {
		  
		ServerPlayer player = (ServerPlayer) evt.getPlayer();
		BlockPos pos = evt.getPos();
		Block source = player.level.getBlockState(pos).getBlock();
		
		VeinMiningLogic.startVeinMining(player, pos, source);
	  }
	  
  }
  
  @EventBusSubscriber
  class CapabilitiesListener {
	  
	  @SubscribeEvent
	  public void registerCapibility(RegisterCapabilitiesEvent evt) {
	    evt.register(IVeinCapability.class);
	  }
  }
}
