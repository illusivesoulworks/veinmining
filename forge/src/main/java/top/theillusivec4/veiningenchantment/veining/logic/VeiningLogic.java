package top.theillusivec4.veiningenchantment.veining.logic;

import com.google.common.collect.Sets;
import java.util.LinkedList;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.block.JigsawBlock;
import net.minecraft.block.StructureBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import top.theillusivec4.veiningenchantment.VeiningEnchantmentMod;
import top.theillusivec4.veiningenchantment.config.VeiningEnchantmentConfig;

public class VeiningLogic {

  private static final Direction[] CARDINAL_DIRECTIONS =
      new Direction[] {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST,
          Direction.NORTH, Direction.SOUTH};

  public static void startVeining(ServerPlayerEntity playerEntity, BlockPos pos, Block source) {
    ServerWorld world = playerEntity.getServerWorld();
    ItemStack stack = playerEntity.getHeldItemMainhand();
    int veiningLevels = EnchantmentHelper.getEnchantmentLevel(VeiningEnchantmentMod.VEINING, stack);
    boolean enabledByCrouching = VeiningEnchantmentConfig.Veining.enabledByCrouching;
    boolean disabled = (playerEntity.isCrouching() && !enabledByCrouching) ||
        (!playerEntity.isCrouching() && enabledByCrouching);

    if (veiningLevels <= 0 || disabled) {
      return;
    }
    int blocks = 0;
    int maxBlocks = VeiningEnchantmentConfig.Veining.maxBlocksPerLevel * veiningLevels;
    int maxDistance = VeiningEnchantmentConfig.Veining.maxDistancePerLevel * veiningLevels;
    Set<BlockPos> visited = Sets.newHashSet(pos);
    LinkedList<Tuple<BlockPos, Integer>> candidates = new LinkedList<>();
    addValidNeighbors(candidates, pos, 1);

    while (!candidates.isEmpty()) {
      Tuple<BlockPos, Integer> candidate = candidates.poll();
      BlockPos blockPos = candidate.getA();
      int blockDistance = candidate.getB();

      if (VeiningEnchantmentConfig.Veining.limitedByDurability &&
          (stack.getDamage() == stack.getMaxDamage() ||
              (VeiningEnchantmentConfig.Veining.preventToolDestruction &&
                  stack.getDamage() == stack.getMaxDamage() - 1))) {
        return;
      }
      Block block = world.getBlockState(blockPos).getBlock();

      if (blocks < maxBlocks && blockDistance < maxDistance && visited.add(blockPos) &&
          BlockChecker.isValidTarget(source, block) && harvest(playerEntity, blockPos, pos)) {
        addValidNeighbors(candidates, blockPos, blockDistance + 1);
        blocks++;
      }
    }
  }

  private static void addValidNeighbors(LinkedList<Tuple<BlockPos, Integer>> candidates,
                                        BlockPos source, int distance) {

    for (Direction direction : CARDINAL_DIRECTIONS) {
      candidates.add(new Tuple<>(source.offset(direction), distance));
    }

    if (VeiningEnchantmentConfig.Veining.diagonalMining) {
      BlockPos up = source.offset(Direction.UP);
      BlockPos down = source.offset(Direction.DOWN);
      Direction[] yOffsets =
          new Direction[] {Direction.SOUTH, Direction.SOUTH, Direction.NORTH, Direction.NORTH};
      Direction[] xOffsets =
          new Direction[] {Direction.EAST, Direction.WEST, Direction.EAST, Direction.WEST};

      for (int i = 0; i < yOffsets.length; i++) {
        candidates.add(new Tuple<>(up.offset(yOffsets[i]).offset(xOffsets[i]), distance));
        candidates.add(new Tuple<>(down.offset(yOffsets[i]).offset(xOffsets[i]), distance));
      }
    }
  }

  public static boolean harvest(ServerPlayerEntity player, BlockPos pos, BlockPos originPos) {
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

          if (VeiningEnchantmentConfig.Veining.addToolDamage) {
            onBlockDestroyed(itemstack, world, blockstate, pos, player);
          }

          if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
            net.minecraftforge.event.ForgeEventFactory
                .onPlayerDestroyItem(player, itemstack1, Hand.MAIN_HAND);
          }
          boolean flag = removeBlock(player, pos, flag1);
          BlockPos spawnPos = VeiningEnchantmentConfig.Veining.relocateDrops ? originPos : pos;

          if (flag && flag1) {
            harvestBlock(block, world, player, pos, spawnPos, blockstate, tileentity, itemstack1);
          }

          if (flag && exp > 0) {
            blockstate.getBlock().dropXpOnBlockBreak(world, spawnPos, exp);
          }
        }
        return true;
      }
    }
  }

  private static void onBlockDestroyed(ItemStack stack, World worldIn, BlockState blockIn,
                                       BlockPos pos, PlayerEntity playerIn) {

    if (!worldIn.isRemote && blockIn.getBlockHardness(worldIn, pos) != 0.0F) {
      int damage = VeiningEnchantmentConfig.Veining.toolDamageMultiplier;

      if (VeiningEnchantmentConfig.Veining.preventToolDestruction) {
        damage = Math.min(damage, stack.getMaxDamage() - stack.getDamage() - 1);
      }

      if (damage > 0) {
        stack.damageItem(damage, playerIn,
            (entity) -> entity.sendBreakAnimation(EquipmentSlotType.MAINHAND));
      }
    }
  }

  private static void harvestBlock(Block block, World worldIn, PlayerEntity player, BlockPos pos,
                                   BlockPos spawnPos, BlockState state, @Nullable TileEntity te,
                                   ItemStack stack) {
    player.addStat(Stats.BLOCK_MINED.get(block));

    if (VeiningEnchantmentConfig.Veining.addPlayerExhaustion) {
      player.addExhaustion(
          (float) (0.005F * (VeiningEnchantmentConfig.Veining.playerExhaustionMultiplier)));
    }

    if (worldIn instanceof ServerWorld) {
      Block.getDrops(state, (ServerWorld) worldIn, pos, te, player, stack)
          .forEach((stackToSpawn) -> Block.spawnAsEntity(worldIn, spawnPos, stackToSpawn));
      state.spawnAdditionalDrops((ServerWorld) worldIn, pos, stack);
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
}
