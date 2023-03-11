/*
 * Copyright (c) 2021 C4
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

package top.theillusivec4.veinmining.veinmining;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class VeinMiningPlayers {

  private static final long DIFF = 20;
  private static final Map<UUID, Long> ACTIVATED_MINERS = new ConcurrentHashMap<>();
  private static final Set<UUID> CURRENT_MINERS = new ConcurrentSkipListSet<>();
  private static final Map<World, Map<BlockPos, BlockPos>> MINING_BLOCKS = new ConcurrentHashMap<>();

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

  public static boolean canStartVeinMining(PlayerEntity player) {
    return ACTIVATED_MINERS.containsKey(player.getUuid());
  }

  public static void activateVeinMining(PlayerEntity player, long time) {
    ACTIVATED_MINERS.put(player.getUuid(), time);
  }

  public static void deactivateVeinMining(PlayerEntity player) {
    ACTIVATED_MINERS.remove(player.getUuid());
  }

  public static boolean isVeinMining(PlayerEntity player) {
    return CURRENT_MINERS.contains(player.getUuid());
  }

  public static void startVeinMining(PlayerEntity player) {
    CURRENT_MINERS.add(player.getUuid());
  }

  public static void stopVeinMining(PlayerEntity player) {
    CURRENT_MINERS.remove(player.getUuid());
  }

  public static void addMiningBlock(World level, BlockPos pos, BlockPos spawnPos) {
    MINING_BLOCKS.computeIfAbsent(level, (k) -> new ConcurrentHashMap<>()).put(pos, spawnPos);
  }

  public static void removeMiningBlock(World level, BlockPos pos) {
    Map<BlockPos, BlockPos> map = MINING_BLOCKS.get(level);

    if (map != null) {
      map.remove(pos);
    }
  }

  public static Optional<BlockPos> getNewSpawnPosForDrop(World level, BlockPos pos) {
    Map<BlockPos, BlockPos> map = MINING_BLOCKS.get(level);

    if (map != null) {
      return Optional.ofNullable(map.get(pos));
    }
    return Optional.empty();
  }
}
