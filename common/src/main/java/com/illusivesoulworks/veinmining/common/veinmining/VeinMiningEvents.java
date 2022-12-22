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

package com.illusivesoulworks.veinmining.common.veinmining;

import com.illusivesoulworks.veinmining.common.veinmining.enchantment.ItemProcessor;
import com.illusivesoulworks.veinmining.common.veinmining.logic.BlockProcessor;
import com.illusivesoulworks.veinmining.common.veinmining.logic.VeinMiningLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class VeinMiningEvents {

  public static void tick(Level level) {

    if (!level.isClientSide()) {
      VeinMiningPlayers.validate(level.getGameTime());
    }
  }

  public static void reloadDatapack() {
    ItemProcessor.rebuild();
    BlockProcessor.rebuild();
  }

  public static void blockBreak(ServerPlayer player, BlockPos pos, BlockState state) {

    if (VeinMiningPlayers.canStartVeinMining(player) && !VeinMiningPlayers.isVeinMining(player)) {
      VeinMiningPlayers.startVeinMining(player);
      VeinMiningLogic.veinMine(player, pos, state);
      VeinMiningPlayers.stopVeinMining(player);
    }
  }
}
