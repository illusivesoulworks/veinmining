package top.theillusivec4.veiningenchantment.veining.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.theillusivec4.veiningenchantment.config.VeiningEnchantmentConfig;

public class BlockProcessor {

  private static final Map<String, Boolean> checkedBlocks = new HashMap<>();
  private static final Map<String, Map<String, Boolean>> checkedPairs = new HashMap<>();

  public static void rebuild() {
    checkedBlocks.clear();
    checkedPairs.clear();
    BlockGroups.init();
  }

  public static boolean isValidTarget(BlockState state, World world, BlockPos pos, Block source) {
    Block block = world.getBlockState(pos).getBlock();
    return !block.isAir(state, world, pos) &&
        checkedBlocks.computeIfAbsent(Objects.requireNonNull(block.getRegistryName()).toString(),
            (name) -> BlockProcessor.checkBlock(block)) && matches(source, block);
  }

  private static boolean matches(Block origin, Block target) {

    if (origin == target) {
      return true;
    } else {
      String originName = Objects.requireNonNull(origin.getRegistryName()).toString();
      String targetName = Objects.requireNonNull(target.getRegistryName()).toString();
      boolean useOriginKey = originName.compareTo(targetName) >= 0;

      if (useOriginKey) {
        return checkedPairs.computeIfAbsent(originName, (name) -> new HashMap<>())
            .computeIfAbsent(targetName, (name) -> checkMatch(origin, target));
      } else {
        return checkedPairs.computeIfAbsent(targetName, (name) -> new HashMap<>())
            .computeIfAbsent(originName, (name) -> checkMatch(origin, target));
      }
    }
  }

  private static boolean checkBlock(Block block) {
    Set<String> ids = new HashSet<>();
    ids.add(Objects.requireNonNull(block.getRegistryName()).toString());
    block.getTags().forEach(tag -> ids.add("#" + tag.toString()));
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
    Set<String> group =
        BlockGroups.getGroup(Objects.requireNonNull(origin.getRegistryName()).toString());

    if (group != null) {
      return group.contains(Objects.requireNonNull(target.getRegistryName()).toString());
    }
    return false;
  }
}
