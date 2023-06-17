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

package com.illusivesoulworks.veinmining.common.veinmining.logic;


import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.platform.Services;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public class BlockGroups {

  private static final Map<String, Set<String>> BLOCK_TO_GROUP = new HashMap<>();

  public static synchronized void init() {
    BLOCK_TO_GROUP.clear();
    VeinMiningConfig.SERVER.groupsList.clearCache();

    for (String group : VeinMiningConfig.SERVER.groupsList.getTransformed()) {
      String[] ids = group.split(",");
      Set<String> blockGroup = createGroup(ids);

      for (String blockId : blockGroup) {
        BLOCK_TO_GROUP.merge(blockId, blockGroup, (s1, s2) -> {
          s1.addAll(s2);
          return s1;
        });
      }
    }
  }

  public static Set<String> getGroup(String id) {
    return BLOCK_TO_GROUP.getOrDefault(id, new HashSet<>());
  }

  private static Set<String> createGroup(String[] ids) {
    Set<String> newGroup = new HashSet<>();

    for (String id : ids) {

      if (id.charAt(0) == '#') {
        ResourceLocation rl = ResourceLocation.tryParse(id.substring(1));

        if (rl != null) {
          newGroup.addAll(Services.PLATFORM.getBlocksFromTag(rl));
        }
      } else {
        ResourceLocation rl = ResourceLocation.tryParse(id);

        if (rl != null) {
          Services.PLATFORM.getBlock(rl).ifPresent(block -> {
            if (block != Blocks.AIR) {
              newGroup.add(id);
            }
          });
        }
      }
    }
    return newGroup;
  }
}
