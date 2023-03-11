package top.theillusivec4.veinmining.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;
import top.theillusivec4.veinmining.veinmining.logic.VeinMiningLogic;

public class VeinMiningMixinHooks {

  public static void tryHarvest(ServerPlayerEntity player, BlockPos pos, BlockState source) {
    if (VeinMiningPlayers.canStartVeinMining(player) && !VeinMiningPlayers.isVeinMining(player)) {
      VeinMiningPlayers.startVeinMining(player);
      VeinMiningLogic.startVeinMining(player, pos, source);
      VeinMiningPlayers.stopVeinMining(player);
    }
  }

  public static BlockPos getActualSpawnPos(World level, BlockPos pos) {
    return VeinMiningPlayers.getNewSpawnPosForDrop(level, pos).orElse(pos);
  }

  public static <T extends LivingEntity> boolean shouldCancelItemDamage(T entity) {
    return entity instanceof PlayerEntity player && VeinMiningPlayers.isVeinMining(player) &&
        !VeinMiningConfig.VeinMining.addToolDamage;
  }

  public static int modifyItemDamage(ItemStack stack, int damage,
                                     ServerPlayerEntity player) {
    int newDamage = damage;

    if (player != null && VeinMiningPlayers.isVeinMining(player)) {
      float multiplier = VeinMiningConfig.VeinMining.toolDamageMultiplier;

      if (multiplier != 1.0f) {
        newDamage = Math.max(0, (int) ((float) newDamage * multiplier));
      }

      if (VeinMiningConfig.VeinMining.preventToolDestruction) {
        newDamage = Math.min(damage, stack.getMaxDamage() - stack.getDamage() - 2);
      }
    }
    return newDamage;
  }
}
