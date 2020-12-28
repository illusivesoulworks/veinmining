package top.theillusivec4.veinmining.veinmining.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
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
          Tag<Block> tag = BlockTags.getTagGroup().getTag(identifier);

          if (tag != null) {

            for (Block block : tag.values()) {
              newGroup.add(Registry.BLOCK.getId(block).toString());
            }
          }
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
