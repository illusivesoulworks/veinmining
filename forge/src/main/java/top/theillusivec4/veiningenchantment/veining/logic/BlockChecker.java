package top.theillusivec4.veiningenchantment.veining.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.veiningenchantment.config.VeiningEnchantmentConfig;

public class BlockChecker {

  private static final Map<Block, Boolean> checkedBlocks = new HashMap<>();
  private static final Map<Block, Map<Block, Boolean>> checkedPairs = new HashMap<>();

  public static void reset() {
    checkedBlocks.clear();
    checkedPairs.clear();
  }

  public static boolean isValidTarget(Block source, Block block) {
    return checkedBlocks.computeIfAbsent(block, BlockChecker::checkBlock) && matches(source, block);
  }

  private static boolean matches(Block origin, Block target) {
    return checkedPairs.computeIfAbsent(origin, (block -> new HashMap<>()))
        .computeIfAbsent(target, (block) -> checkMatch(origin, block));
  }

  private static boolean checkBlock(Block block) {
    Set<String> ids = new HashSet<>();
    ids.add(Objects.requireNonNull(block.getRegistryName()).toString());
    block.getTags().forEach(tag -> ids.add(tag.toString()));
    Set<String> configs = VeiningEnchantmentConfig.Veining.blocks;

    if (VeiningEnchantmentConfig.Veining.blocksPermission ==
        VeiningEnchantmentConfig.PermissionType.BLACKLIST) {

      for (String id : configs) {

        if (ids.contains(id)) {
          return false;
        }
      }
      return true;
    } else {

      for (String id : configs) {

        if (ids.contains(id)) {
          return true;
        }
      }
      return false;
    }
  }

  private static boolean checkMatch(Block origin, Block target) {

    if (origin == target) {
      return true;
    } else {
      Set<ResourceLocation> originTags = origin.getTags();
      Set<ResourceLocation> targetTags = target.getTags();

      for (ResourceLocation originTag : originTags) {

        if (targetTags.contains(originTag)) {
          return true;
        }
      }
      return false;
    }
  }
}
