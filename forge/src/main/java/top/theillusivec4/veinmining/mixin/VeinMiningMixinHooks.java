package top.theillusivec4.veinmining.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.veinmining.VeinMiningPlayers;

public class VeinMiningMixinHooks {

  public static BlockPos getActualSpawnPos(Level level, BlockPos pos) {
    return VeinMiningPlayers.getNewSpawnPosForDrop(level, pos).orElse(pos);
  }

  public static <T extends LivingEntity> boolean shouldCancelItemDamage(T entity) {
    return entity instanceof Player player && VeinMiningPlayers.isVeinMining(player) &&
        !VeinMiningConfig.VeinMining.addToolDamage;
  }

  public static int modifyItemDamage(ItemStack stack, int damage,
                                     ServerPlayer player) {
    int newDamage = damage;

    if (player != null && VeinMiningPlayers.isVeinMining(player)) {
      float multiplier = VeinMiningConfig.VeinMining.toolDamageMultiplier;

      if (multiplier != 1.0f) {
        newDamage = Math.max(0, (int) ((float) newDamage * multiplier));
      }

      if (VeinMiningConfig.VeinMining.preventToolDestruction) {
        newDamage = Math.min(damage, stack.getMaxDamage() - stack.getDamageValue() - 2);
      }
    }
    return newDamage;
  }
}
