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

package com.illusivesoulworks.veinmining.common;

import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningEvents;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

public class NeoForgeCommonEventsListener {

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void levelTick(final TickEvent.LevelTickEvent evt) {
    VeinMiningEvents.tick(evt.level);
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
        VeinMiningEvents.reloadDatapack();
      }
    });
  }

  @SubscribeEvent
  public void serverStarted(final ServerStartedEvent evt) {
    VeinMiningEvents.reloadDatapack();
  }

  @SubscribeEvent(priority = EventPriority.LOW)
  @SuppressWarnings("unused")
  public void blockBreak(final BlockEvent.BreakEvent evt) {

    if (evt.getPlayer() instanceof ServerPlayer player) {
      VeinMiningEvents.blockBreak(player, evt.getPos(), evt.getState());
    }
  }

  @SubscribeEvent
  public void playerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent evt) {

    if (evt.getEntity() instanceof ServerPlayer player) {
      VeinMiningEvents.playerLoggedOut(player);
    }
  }
}
