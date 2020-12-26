package top.theillusivec4.veiningenchantment.veining;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import top.theillusivec4.veiningenchantment.VeiningEnchantmentMod;

public class VeiningManager {

  private static final Direction[] CARDINAL =
      new Direction[] {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST,
          Direction.NORTH, Direction.SOUTH};

  public static void veining(ServerPlayerEntity playerEntity, BlockPos pos, Block source) {
    ServerWorld world = playerEntity.getServerWorld();
    int veiningLevels = EnchantmentHelper
        .getEnchantmentLevel(VeiningEnchantmentMod.VEINING, playerEntity.getHeldItemMainhand());

    if (veiningLevels <= 0 || playerEntity.isCrouching()) {
      return;
    }
    int blocks = 100;
    Set<BlockPos> visited = new HashSet<>();
    visited.add(pos);
    LinkedList<BlockPos> candidates = new LinkedList<>();
    search(candidates, pos);

    while (!candidates.isEmpty()) {
      BlockPos candidate = candidates.poll();

      if (blocks > 0 && visited.add(candidate) &&
          matches(source, world.getBlockState(candidate).getBlock()) &&
          harvest(playerEntity, candidate)) {
        search(candidates, candidate);
        blocks--;
      }
    }
  }

  private static void search(LinkedList<BlockPos> candidates, BlockPos source) {

    for (Direction direction : CARDINAL) {
      candidates.add(source.offset(direction));
    }
  }

  public static boolean harvest(ServerPlayerEntity player, BlockPos pos) {
    ServerWorld world = player.getServerWorld();
    BlockState blockstate = world.getBlockState(pos);
    GameType gameType = player.interactionManager.getGameType();
    int exp = net.minecraftforge.common.ForgeHooks.onBlockBreakEvent(world, gameType, player, pos);

    if (exp == -1) {
      return false;
    } else {
      TileEntity tileentity = world.getTileEntity(pos);
      Block block = blockstate.getBlock();

      if ((block instanceof CommandBlockBlock || block instanceof StructureBlock ||
          block instanceof JigsawBlock) && !player.canUseCommandBlock()) {
        world.notifyBlockUpdate(pos, blockstate, blockstate, 3);
        return false;
      } else if (player.getHeldItemMainhand().onBlockStartBreak(pos, player)) {
        return false;
      } else if (player.blockActionRestricted(world, pos, gameType)) {
        return false;
      } else {

        if (gameType.isCreative()) {
          removeBlock(player, pos, false);
        } else {
          ItemStack itemstack = player.getHeldItemMainhand();
          ItemStack itemstack1 = itemstack.copy();
          boolean flag1 = blockstate.canHarvestBlock(world, pos, player);
          itemstack.onBlockDestroyed(world, blockstate, pos, player);
          if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
            net.minecraftforge.event.ForgeEventFactory
                .onPlayerDestroyItem(player, itemstack1, Hand.MAIN_HAND);
          }
          boolean flag = removeBlock(player, pos, flag1);

          if (flag && flag1) {
            block.harvestBlock(world, player, pos, blockstate, tileentity, itemstack1);
          }

          if (flag && exp > 0) {
            blockstate.getBlock().dropXpOnBlockBreak(world, pos, exp);
          }
        }
        return true;
      }
    }
  }

  private static boolean removeBlock(PlayerEntity player, BlockPos pos, boolean canHarvest) {
    World world = player.getEntityWorld();
    BlockState state = world.getBlockState(pos);
    boolean removed =
        state.removedByPlayer(world, pos, player, canHarvest, world.getFluidState(pos));

    if (removed) {
      state.getBlock().onPlayerDestroy(world, pos, state);
    }
    return removed;
  }

  private static boolean matches(Block origin, Block target) {

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
