/*
 * Copyright (c) 2021 C4
 *
 * This file is part of Vein Mining, a mod made for Minecraft.
 *
 * Vein Mining is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * Vein Mining is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Vein Mining.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.veinmining.veinmining;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class VeinMiningKey {

  private static final KeyBinding key =
      new KeyBinding("key.veinmining.activate.desc", InputUtil.UNKNOWN_KEY.getCode(),
          "key.veinmining.category");

  public static KeyBinding get() {
    return key;
  }
}
