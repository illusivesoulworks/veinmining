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

package com.illusivesoulworks.veinmining.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import com.illusivesoulworks.veinmining.VeinMiningConstants;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class EarlyRiser implements Runnable {

  @Override
  public void run() {
    MappingResolver remapper = FabricLoader.getInstance().getMappingResolver();

    String enchantmentCategory = remapper.mapClassName("intermediary", "net.minecraft.class_1886");
    ClassTinkerers.enumBuilder(enchantmentCategory)
        .addEnumSubclass(VeinMiningConstants.ENCHANTMENT_ID.toString(),
            "com.illusivesoulworks.veinmining.mixin.VeinMiningCategory")
        .build();
  }
}
