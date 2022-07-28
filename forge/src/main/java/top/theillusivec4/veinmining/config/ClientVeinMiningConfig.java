/*
 * Copyright (C) 2020-2021 C4
 *
 * This file is part of Vein Mining.
 *
 * Vein Mining is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and the GNU Lesser General Public License along with Vein Mining.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */

package top.theillusivec4.veinmining.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import top.theillusivec4.veinmining.VeinMiningMod;

public class ClientVeinMiningConfig {

  public static final ForgeConfigSpec CONFIG_SPEC;
  public static final Config CONFIG;
  private static final String CONFIG_PREFIX = "gui." + VeinMiningMod.MOD_ID + ".config.";

  static {
    final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
        .configure(Config::new);
    CONFIG_SPEC = specPair.getRight();
    CONFIG = specPair.getLeft();
  }

  public static VeinMiningConfig.ActivationState activationState =
      VeinMiningConfig.ActivationState.STANDING;
  public static VeinMiningConfig.ActivationState activationStateWithoutEnchantment =
      VeinMiningConfig.ActivationState.KEYBINDING;

  public static void bake() {
    activationState = CONFIG.activationState.get();
    activationStateWithoutEnchantment = CONFIG.activationStateWithoutEnchantment.get();
  }

  public static class Config {

    public final ForgeConfigSpec.EnumValue<VeinMiningConfig.ActivationState> activationState;
    public final ForgeConfigSpec.EnumValue<VeinMiningConfig.ActivationState>
        activationStateWithoutEnchantment;

    public Config(ForgeConfigSpec.Builder builder) {
      builder.push("vein mining");

      activationState = builder.comment(
              "Whether to activate vein mining (if using with the enchantment) by standing, crouching, or holding down the keybind")
          .translation(CONFIG_PREFIX + "activationState")
          .defineEnum("activationState", VeinMiningConfig.ActivationState.STANDING);

      activationStateWithoutEnchantment = builder.comment(
              "Whether to activate vein mining (if using without the enchantment) by standing, crouching, or holding down the keybind")
          .translation(CONFIG_PREFIX + "activationStateWithoutEnchantment")
          .defineEnum("activationStateWithoutEnchantment",
              VeinMiningConfig.ActivationState.KEYBINDING);

      builder.pop();
    }
  }
}
