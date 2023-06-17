package com.illusivesoulworks.veinmining;

import com.illusivesoulworks.spectrelib.config.SpectreLibInitializer;
import org.quiltmc.loader.api.ModContainer;

public class VeinMiningConfigInitializer implements SpectreLibInitializer {

  @Override
  public void onInitializeConfig(ModContainer modContainer) {
    VeinMiningMod.init();
  }
}
