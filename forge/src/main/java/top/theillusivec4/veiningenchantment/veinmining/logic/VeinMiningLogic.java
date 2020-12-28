package top.theillusivec4.veiningenchantment.veinmining.logic;

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
import top.theillusivec4.veiningenchantment.VeinMiningMod;
import top.theillusivec4.veiningenchantment.config.VeinMiningConfig;

public class VeinMiningLogic {

  private static final Direction[] CARDINAL_DIRECTIONS =
      new Direction[] {Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST,
          Direction.NORTH, Direction.SOUTH};

  public static void startVeining(ServerPlayerEntity playerEntity, BlockPos pos, Block source) {
    ServerWorld world = playerEntity.getServerWorld();
    ItemStack stack = playerEntity.getHeldItemMainhand();
    int veiningLevels = EnchantmentHelper.getEnchantmentLevel(VeinMiningMod.VEIN_MINING, stack);

    if (veiningLevels <= 0) {
      return;
    }
    VeinMiningConfig.ActivationState activationState =
        VeinMiningConfig.VeinMining.activationState;
    boolean disabled = (playerEntity.isCrouching() &&
        activationState == VeinMiningConfig.ActivationState.STANDING) ||
        (!playerEntity.isCrouching() &&
            activationState == VeinMiningConfig.ActivationState.CROUCHING);

    if (disabled) {
      return;
    }
    int blocks = 0;
    int maxBlocks = VeinMiningConfig.VeinMining.maxBlocksPerLevel * veiningLevels;
    int maxDistance = VeinMiningConfig.VeinMining.maxDistancePerLevel * veiningLevels;
    Set<BlockPos> visited = Sets.newHashSet(pos);
    LinkedList<Tuple<BlockPos, Integer>> candidates = new LinkedList<>();
    addValidNeighbors(candidates, pos, 1);

    while (!candidates.isEmpty() && blocks < maxBlocks) {
      Tuple<BlockPos, Integer> candidate = candidates.poll();
      BlockPos blockPos = candidate.getA();
      int blockDistance = candidate.getB();

      if (stopVeining(stack)) {
        return;
      }
      BlockState state = world.getBlockState(blockPos);

      if (visited.add(blockPos) && BlockProcessor.isValidTarget(state, world, blockPos, source) &&
          harvest(playerEntity, blockPos, pos)) {

        if (blockDistance < maxDistance) {
          addValidNeighbors(candidates, blockPos, blockDistance + 1);
        }
        blocks++;
      }
    }
  }

  private static boolean stopVeining(ItemStack stack) {
    return VeinMiningConfig.VeinMining.limitedByDurability &&
        (stack.getDamage() == stack.getMaxDamage() ||
            (VeinMiningConfig.VeinMining.preventToolDestruction &&
                stack.getDamage() == stack.getMaxDamage() - 1));
  }

  private static void addValidNeighbors(LinkedList<Tuple<BlockPos, Integer>> candidates,
                                        BlockPos source, int distance) {

    if (VeinMiningConfig.VeinMining.diagonalMining) {
      BlockPos up = source.up();
      BlockPos down = source.down();
      candidates.add(new Tuple<>(up, distance));
      candidates.add(new Tuple<>(down, distance));
      BlockPos[] blockPositions = new BlockPos[] {up, down, source};

      for (BlockPos blockPos : blockPositions) {
        candidates.add(new Tuple<>(blockPos.west(), distance));
        candidates.add(new Tuple<>(blockPos.east(), distance));
        candidates.add(new Tuple<>(blockPos.north(), distance));
        candidates.add(new Tuple<>(blockPos.south(), distance));
        candidates.add(new Tuple<>(blockPos.north().west(), distance));
        candidates.add(new Tuple<>(blockPos.north().east(), distance));
        candidates.add(new Tuple<>(blockPos.south().west(), distance));
        candidates.add(new Tuple<>(blockPos.south().east(), distance));
      }
    } else {

      for (Direction direction : CARDINAL_DIRECTIONS) {
        candidates.add(new Tuple<>(source.offset(direction), distance));
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

          if (VeinMiningConfig.VeinMining.addToolDamage) {
            onBlockDestroyed(itemstack, world, blockstate, pos, player);
          }

          if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
            net.minecraftforge.event.ForgeEventFactory
                .onPlayerDestroyItem(player, itemstack1, Hand.MAIN_HAND);
          }
          boolean flag = removeBlock(player, pos, flag1);
          BlockPos spawnPos = VeinMiningConfig.VeinMining.relocateDrops ? originPos : pos;

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
      int damage = VeinMiningConfig.VeinMining.toolDamageMultiplier;

      if (VeinMiningConfig.VeinMining.preventToolDestruction) {
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

    if (VeinMiningConfig.VeinMining.addPlayerExhaustion) {
      player.addExhaustion(
          (float) (0.005F * (VeinMiningConfig.VeinMining.playerExhaustionMultiplier)));
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
