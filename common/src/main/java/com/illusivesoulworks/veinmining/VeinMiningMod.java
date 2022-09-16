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

package com.illusivesoulworks.veinmining;

import com.illusivesoulworks.spectrelib.config.SpectreConfig;
import com.illusivesoulworks.spectrelib.config.SpectreConfigLoader;
import com.illusivesoulworks.veinmining.common.config.VeinMiningConfig;
import com.illusivesoulworks.veinmining.common.veinmining.enchantment.VeinMiningEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;

public class VeinMiningMod {

  public static Enchantment ENCHANTMENT = new VeinMiningEnchantment();

  public static void init() {
    String id = VeinMiningConstants.MOD_ID;
    SpectreConfigLoader.add(SpectreConfig.Type.SERVER, VeinMiningConfig.SERVER_SPEC, id);
    SpectreConfigLoader.add(SpectreConfig.Type.COMMON, VeinMiningConfig.COMMON_SPEC, id);
    SpectreConfigLoader.add(SpectreConfig.Type.CLIENT, VeinMiningConfig.CLIENT_SPEC, id);
  }
}