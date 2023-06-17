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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class VeinMiningPlayers {

  private static final long DIFF = 20;
  private static final Map<UUID, Long> ACTIVATED_MINERS = new ConcurrentHashMap<>();
  private static final Map<UUID, Long> CURRENT_MINERS = new ConcurrentHashMap<>();
  private static final Map<Level, Map<BlockPos, BlockPos>> MINING_BLOCKS =
      new ConcurrentHashMap<>();

  public static void validate(long worldTime) {
    Iterator<Map.Entry<UUID, Long>> entries = ACTIVATED_MINERS.entrySet().iterator();

    while (entries.hasNext()) {
      Map.Entry<UUID, Long> entry = entries.next();
      long lastTime = entry.getValue();

      if (worldTime - lastTime > DIFF || lastTime > worldTime) {
        entries.remove();
      }
    }
  }

  public static boolean canStartVeinMining(Player player) {
    return ACTIVATED_MINERS.containsKey(player.getUUID());
  }

  public static void activateVeinMining(Player player, long time) {
    ACTIVATED_MINERS.put(player.getUUID(), time);
  }

  public static void deactivateVeinMining(Player player) {
    ACTIVATED_MINERS.remove(player.getUUID());
  }

  public static boolean isVeinMining(Player player) {
    return CURRENT_MINERS.containsKey(player.getUUID());
  }

  public static void startVeinMining(Player player) {
    CURRENT_MINERS.put(player.getUUID(), player.level().getGameTime());
  }

  public static void stopVeinMining(Player player) {
    CURRENT_MINERS.remove(player.getUUID());
  }

  public static void addMiningBlock(Level level, BlockPos pos, BlockPos spawnPos) {
    MINING_BLOCKS.computeIfAbsent(level, (k) -> new HashMap<>()).put(pos, spawnPos);
  }

  public static void removeMiningBlock(Level level, BlockPos pos) {
    Map<BlockPos, BlockPos> map = MINING_BLOCKS.get(level);

    if (map != null) {
      map.remove(pos);
    }
  }

  public static Optional<BlockPos> getNewSpawnPosForDrop(Level level, BlockPos pos) {
    Map<BlockPos, BlockPos> map = MINING_BLOCKS.get(level);

    if (map != null) {
      return Optional.ofNullable(map.get(pos));
    }
    return Optional.empty();
  }
}
