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
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;
import top.theillusivec4.veinmining.veinmining.logic.BlockProcessor;

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
}
