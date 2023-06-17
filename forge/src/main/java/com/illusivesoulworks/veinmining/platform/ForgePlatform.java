/*
 * Copyright (C) 2020-2022 Illusive Soulworks
 *
 * Vein Mining is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Vein Mining.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.veinmining.platform;

import com.google.common.collect.ImmutableMap;
import com.illusivesoulworks.veinmining.VeinMiningConstants;
import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.platform.services.IPlatform;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningPlayers;
import com.illusivesoulworks.veinmining.common.veinmining.enchantment.VeinMiningEnchantment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

public class ForgePlatform implements IPlatform {

  private static final EnchantmentCategory CATEGORY =
      EnchantmentCategory.create(VeinMiningConstants.ENCHANTMENT_ID.toString(),
          VeinMiningEnchantment::canEnchant);

  @Override
  public Set<String> getBlocksFromTag(ResourceLocation resourceLocation) {
    Set<String> result = new HashSet<>();
    ITagManager<Block> tagManager = ForgeRegistries.BLOCKS.tags();

    if (tagManager != null) {

      for (Block block : tagManager.getTag(
          tagManager.createOptionalTagKey(resourceLocation, new HashSet<>()))) {
        ResourceLocation rl = ForgeRegistries.BLOCKS.getKey(block);

        if (rl != null) {
          result.add(rl.toString());
        }
      }
    }
    return result;
  }

  @Override
  public EnchantmentCategory getEnchantmentCategory() {
    return CATEGORY;
  }

  @Override
  public Optional<Enchantment> getEnchantment(ResourceLocation resourceLocation) {
    return Optional.ofNullable(ForgeRegistries.ENCHANTMENTS.getValue(resourceLocation));
  }

  @Override
  public Optional<Item> getItem(ResourceLocation resourceLocation) {
    return Optional.ofNullable(ForgeRegistries.ITEMS.getValue(resourceLocation));
  }

  @Override
  public Optional<Block> getBlock(ResourceLocation resourceLocation) {
    return Optional.ofNullable(ForgeRegistries.BLOCKS.getValue(resourceLocation));
  }

  @Override
  public Optional<ResourceLocation> getResourceLocation(Enchantment enchantment) {
    return Optional.ofNullable(ForgeRegistries.ENCHANTMENTS.getKey(enchantment));
  }

  @Override
  public Optional<ResourceLocation> getResourceLocation(Item item) {
    return Optional.ofNullable(ForgeRegistries.ITEMS.getKey(item));
  }

  @Override
  public Optional<ResourceLocation> getResourceLocation(Block block) {
    return Optional.ofNullable(ForgeRegistries.BLOCKS.getKey(block));
  }

  @Override
  public Map<String, Predicate<ItemStack>> buildEnchantableItems() {
    Map<String, Predicate<ItemStack>> result = new HashMap<>();
    result.put("is:tool", ForgePlatform::canToolAction);
    result.put("is:pickaxe", stack -> canToolAction(ToolActions.PICKAXE_DIG, stack));
    result.put("is:axe", stack -> canToolAction(ToolActions.AXE_DIG, stack));
    result.put("is:hoe", stack -> canToolAction(ToolActions.HOE_DIG, stack));
    result.put("is:shovel", stack -> canToolAction(ToolActions.SHOVEL_DIG, stack));
    return ImmutableMap.copyOf(result);
  }

  @Override
  public boolean canHarvestDrops(ServerPlayer playerEntity, BlockState state) {
    return ForgeHooks.isCorrectToolForDrops(state, playerEntity);
  }

  private static boolean canToolAction(ToolAction toolAction, ItemStack stack) {
    return stack.canPerformAction(toolAction);
  }

  private static boolean canToolAction(ItemStack stack) {
    Set<ToolAction> actions =
        Set.of(ToolActions.PICKAXE_DIG, ToolActions.AXE_DIG, ToolActions.HOE_DIG,
            ToolActions.SHOVEL_DIG);

    for (ToolAction action : actions) {

      if (stack.canPerformAction(action)) {
        return true;
      }
    }
    return false;
  }

  public boolean harvest(ServerPlayer player, BlockPos pos, BlockPos originPos) {
    ServerLevel world = player.serverLevel();
    BlockState blockstate = world.getBlockState(pos);
    GameType gameType = player.gameMode.getGameModeForPlayer();
    int exp = ForgeHooks.onBlockBreakEvent(world, gameType, player, pos);

    if (exp == -1) {
      return false;
    } else {
      BlockEntity blockentity = world.getBlockEntity(pos);
      Block block = blockstate.getBlock();

      if ((block instanceof CommandBlock || block instanceof StructureBlock ||
          block instanceof JigsawBlock) && !player.canUseGameMasterBlocks()) {
        world.sendBlockUpdated(pos, blockstate, blockstate, 3);
        return false;
      } else if (player.getMainHandItem().onBlockStartBreak(pos, player)) {
        return true;
      } else if (player.blockActionRestricted(world, pos, gameType)) {
        return false;
      } else {

        if (gameType.isCreative()) {
          removeBlock(player, pos, false);
        } else {
          ItemStack itemstack = player.getMainHandItem();
          ItemStack itemstack1 = itemstack.copy();
          boolean flag1 = blockstate.canHarvestBlock(world, pos, player);
          itemstack.mineBlock(world, blockstate, pos, player);

          if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
            ForgeEventFactory.onPlayerDestroyItem(player, itemstack1, InteractionHand.MAIN_HAND);
          }
          boolean flag = removeBlock(player, pos, flag1);
          BlockPos spawnPos = VeinMiningConfig.SERVER.relocateDrops.get() ? originPos : pos;

          if (flag && flag1) {
            FoodData foodData = player.getFoodData();
            float currentExhaustion = foodData.getExhaustionLevel();
            VeinMiningPlayers.addMiningBlock(world, pos, spawnPos);
            block.playerDestroy(world, player, pos, blockstate, blockentity, itemstack1);
            VeinMiningPlayers.removeMiningBlock(world, pos);

            if (VeinMiningConfig.SERVER.addExhaustion.get()) {
              float diff = foodData.getExhaustionLevel() - currentExhaustion;
              foodData.setExhaustion(currentExhaustion);
              foodData.addExhaustion(
                  (float) (diff * VeinMiningConfig.SERVER.exhaustionMultiplier.get()));
            } else {
              foodData.setExhaustion(currentExhaustion);
            }
          }

          if (flag && exp > 0) {
            blockstate.getBlock().popExperience(world, spawnPos, exp);
          }
        }
        return true;
      }
    }
  }

  @Override
  public Set<String> getItemsFromTag(ResourceLocation resourceLocation) {
    Set<String> result = new HashSet<>();
    ITagManager<Item> tagManager = ForgeRegistries.ITEMS.tags();

    if (tagManager != null) {

      for (Item item : tagManager.getTag(
          tagManager.createOptionalTagKey(resourceLocation, new HashSet<>()))) {
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(item);

        if (rl != null) {
          result.add(rl.toString());
        }
      }
    }
    return result;
  }

  @Override
  public List<String> getDefaultItemsConfig() {
    return Arrays.asList("is:tool", "quark:pickarang", "quark:flamerang");
  }

  @Override
  public List<String> getDefaultGroups() {
    return Arrays.asList(
        "#forge:obsidian",
        "#forge:ores/coal",
        "#forge:ores/diamond",
        "#forge:ores/emerald",
        "#forge:ores/gold",
        "#forge:ores/iron",
        "#forge:ores/lapis",
        "#forge:ores/redstone",
        "#forge:ores/quartz",
        "#forge:ores/netherite_scrap",
        "#forge:ores/copper",
        "#forge:ores/tin",
        "#forge:ores/osmium",
        "#forge:ores/uranium",
        "#forge:ores/fluorite",
        "#forge:ores/lead",
        "#forge:ores/zinc",
        "#forge:ores/aluminum",
        "#forge:ores/nickel",
        "#forge:ores/silver",
        "#forge:ores/apatite",
        "#forge:ores/cinnabar",
        "#forge:ores/niter",
        "#forge:ores/ruby",
        "#forge:ores/sapphire",
        "#forge:ores/sulfur"
    );
  }

  private static boolean removeBlock(Player player, BlockPos pos, boolean canHarvest) {
    Level world = player.getCommandSenderWorld();
    BlockState state = world.getBlockState(pos);
    boolean removed =
        state.onDestroyedByPlayer(world, pos, player, canHarvest, world.getFluidState(pos));

    if (removed) {
      state.getBlock().destroy(world, pos, state);

      if (!world.getBlockState(pos).isAir()) {
        world.removeBlock(pos, false);
      }
    }
    return removed;
  }
}
