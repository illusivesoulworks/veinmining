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

import com.illusivesoulworks.veinmining.common.network.CPacketState;
import com.illusivesoulworks.veinmining.common.platform.services.IClientPlatform;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningKey;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;

public class FabricClientPlatform implements IClientPlatform {

  @Override
  public KeyMapping createKeyMapping(int key, String desc, KeyMapping.Category category) {
    return new VeinMiningKey.Mapping(desc, key, category);
  }

  @Override
  public void sendC2SState(boolean enabled) {
    ClientPlayNetworking.send(new CPacketState(enabled));
  }
}
