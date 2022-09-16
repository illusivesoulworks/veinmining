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

package com.illusivesoulworks.veinmining.common.platform.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface IPlatform {

  Set<String> getBlocksFromTag(ResourceLocation resourceLocation);

  EnchantmentCategory getEnchantmentCategory();

  Optional<Enchantment> getEnchantment(ResourceLocation resourceLocation);

  Optional<Item> getItem(ResourceLocation resourceLocation);

  Optional<Block> getBlock(ResourceLocation resourceLocation);

  Optional<ResourceLocation> getResourceLocation(Enchantment enchantment);

  Optional<ResourceLocation> getResourceLocation(Item item);

  Optional<ResourceLocation> getResourceLocation(Block block);

  Map<String, Predicate<ItemStack>> buildEnchantableItems();

  boolean canHarvestDrops(ServerPlayer playerEntity, BlockState state);

  boolean harvest(ServerPlayer player, BlockPos pos, BlockPos originPos);

  Set<String> getItemsFromTag(ResourceLocation resourceLocation);

  List<String> getDefaultItemsConfig();

  List<String> getDefaultGroups();
}
