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

package com.illusivesoulworks.veinmining.common.veinmining;

import com.illusivesoulworks.veinmining.common.platform.ClientServices;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class VeinMiningKey {

  private static KeyMapping key;

  public static void setup() {
    key = ClientServices.PLATFORM.createKeyMapping(InputConstants.UNKNOWN.getValue(),
        "key.veinmining.activate.desc", "key.veinmining.category");
  }

  public static KeyMapping get() {
    return key;
  }

  public static final class Mapping extends KeyMapping {

    public Mapping(String pName, int pKeyCode, String pCategory) {
      super(pName, pKeyCode, pCategory);
    }
  }
}
