package com.illusivesoulworks.veinmining.common.veinmining.logic;

import com.illusivesoulworks.veinmining.common.data.BlockGroupsResourceListener;
import com.illusivesoulworks.veinmining.common.platform.Services;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class BlockGroups {

  private static final Map<String, Set<String>> BLOCK_TO_GROUP = new HashMap<>();

  public static synchronized void rebuild() {
    BLOCK_TO_GROUP.clear();
    List<Set<String>> groups = new ArrayList<>(BlockGroupsResourceListener.INSTANCE.getGroups());

    for (Set<String> group : groups) {
      Set<String> blockGroup = createGroup(group);

      for (String blockId : blockGroup) {
        BLOCK_TO_GROUP.merge(blockId, new HashSet<>(blockGroup), (s1, s2) -> {
          s1.addAll(s2);
          return s1;
        });
      }
    }
  }

  public static Set<String> getGroup(String id) {
    return BLOCK_TO_GROUP.getOrDefault(id, new HashSet<>());
  }

  private static Set<String> createGroup(Set<String> ids) {
    Set<String> newGroup = new HashSet<>();

    for (String id : ids) {

      if (id.charAt(0) == '#') {
        Identifier rl = Identifier.tryParse(id.substring(1));

        if (rl != null) {
          newGroup.addAll(Services.PLATFORM.getBlocksFromTag(rl));
        }
      } else {
        Identifier rl = Identifier.tryParse(id);

        if (rl != null) {
          Block block = Services.PLATFORM.getBlock(rl);

          if (block != Blocks.AIR) {
            newGroup.add(id);
          }
        }
      }
    }
    return newGroup;
  }
}
