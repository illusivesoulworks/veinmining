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
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
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
        Identifier identifier = Identifier.tryParse(id.substring(1));

        if (identifier != null) {
          Registry.BLOCK.getEntryList(TagKey.of(Registry.BLOCK_KEY, identifier))
              .ifPresent(registryEntries -> {

                for (RegistryEntry<Block> registryEntry : registryEntries) {
                  registryEntry.getKey().ifPresent(
                      blockRegistryKey -> newGroup.add(blockRegistryKey.getValue().toString()));
                }
              });
        }
      } else {
        Identifier identifier = Identifier.tryParse(id);

        if (identifier != null) {

          if (Registry.BLOCK.containsId(identifier)) {
            newGroup.add(id);
          }
        }
      }
    }
    return newGroup;
  }
}
