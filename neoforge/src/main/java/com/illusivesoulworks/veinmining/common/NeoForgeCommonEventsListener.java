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

import com.illusivesoulworks.veinmining.VeinMiningConstants;
import net.minecraft.resources.Identifier;
import com.illusivesoulworks.veinmining.common.data.BlockGroupsResourceListener;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningEvents;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

public class NeoForgeCommonEventsListener {

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void levelTick(final LevelTickEvent.Post evt) {
    VeinMiningEvents.tick(evt.getLevel());
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void reload(final AddServerReloadListenersEvent evt) {
    BlockGroupsResourceListener.INSTANCE = new BlockGroupsResourceListener();
    evt.addListener(
        Identifier.fromNamespaceAndPath(VeinMiningConstants.MOD_ID, "block_groups"),
        BlockGroupsResourceListener.INSTANCE);
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void serverStarted(final ServerStartedEvent evt) {
    VeinMiningEvents.reloadDatapack();
  }

  @SubscribeEvent(priority = EventPriority.LOW)
  @SuppressWarnings("unused")
  public void blockBreak(final BreakBlockEvent evt) {

    if (evt.getPlayer() instanceof ServerPlayer player) {
      VeinMiningEvents.blockBreak(player, evt.getPos(), evt.getState());
    }
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void toolEquip(final LivingEquipmentChangeEvent evt) {
    VeinMiningEvents.toolEquip(evt.getTo(), evt.getFrom(), evt.getSlot(), evt.getEntity());
  }

  @SubscribeEvent
  @SuppressWarnings("unused")
  public void playerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent evt) {

    if (evt.getEntity() instanceof ServerPlayer player) {
      VeinMiningEvents.playerLoggedOut(player);
    }
  }
}
