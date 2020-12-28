/*
 * Copyright (c) 2020 C4
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

package top.theillusivec4.veinmining.veinmining.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class BlockGroups {

  private static final Map<String, Set<String>> blockToGroup = new HashMap<>();

  public static void init() {
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
        ResourceLocation rl = ResourceLocation.tryCreate(id.substring(1));

        if (rl != null) {
          ITag<Block> tag = BlockTags.getCollection().get(rl);

          if (tag != null) {

            for (Block block : tag.getAllElements()) {
              newGroup.add(Objects.requireNonNull(block.getRegistryName()).toString());
            }
          }
        }
      } else {
        ResourceLocation rl = ResourceLocation.tryCreate(id);

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
