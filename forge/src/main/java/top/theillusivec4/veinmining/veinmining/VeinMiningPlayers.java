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

package top.theillusivec4.veinmining.veinmining;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;

public class VeinMiningPlayers {

  private static final long DIFF = 20;
  private static final Map<UUID, Long> players = new HashMap<>();

  public static void validate(long worldTime) {
    Iterator<Map.Entry<UUID, Long>> entries = players.entrySet().iterator();

    while (entries.hasNext()) {
      Map.Entry<UUID, Long> entry = entries.next();
      long lastTime = entry.getValue();

      if (worldTime - lastTime > DIFF || lastTime > worldTime) {
        entries.remove();
      }
    }
  }

  public static boolean canVeinMine(Player player) {
    return players.containsKey(player.getUUID());
  }

  public static void startVeinMining(Player player, long time) {
    players.put(player.getUUID(), time);
  }

  public static void stopVeinMining(Player player) {
    players.remove(player.getUUID());
  }
}
