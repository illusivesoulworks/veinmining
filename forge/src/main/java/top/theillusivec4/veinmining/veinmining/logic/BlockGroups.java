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

package top.theillusivec4.veinmining.veinmining.logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class BlockGroups {

  private static final Map<String, Set<String>> blockToGroup = new HashMap<>();

  public static synchronized void init() {
    blockToGroup.clear();

    for (String group : VeinMiningConfig.VeinMining.groups) {
      String[] ids = group.split(",");
      Set<String> blockGroup = createGroup(ids);

      for (String blockId : blockGroup) {
        blockToGroup.merge(blockId, blockGroup, (s1, s2) -> {
          s1.addAll(s2);
          return s1;
        });
      }
    }
  }

  public static Set<String> getGroup(String id) {
    return blockToGroup.getOrDefault(id, new HashSet<>());
  }

  private static Set<String> createGroup(String[] ids) {
    Set<String> newGroup = new HashSet<>();
    for (String id : ids) {
      boolean isTag = id.charAt(0) == '#';

      if (isTag) {
        ResourceLocation rl = ResourceLocation.tryParse(id.substring(1));

        if (rl != null) {
          Registry.BLOCK.getTag(TagKey.create(Registry.BLOCK_REGISTRY, rl)).ifPresent(holders -> {

            for (Holder<Block> holder : holders) {
              newGroup.add(ForgeRegistries.BLOCKS.getKey(holder.value()).toString());
            }
          });
        }
      } else {
        ResourceLocation rl = ResourceLocation.tryParse(id);

        if (rl != null) {

          if (ForgeRegistries.BLOCKS.containsKey(rl)) {
            newGroup.add(id);
          }
        }
      }
    }
    return newGroup;
  }
}
