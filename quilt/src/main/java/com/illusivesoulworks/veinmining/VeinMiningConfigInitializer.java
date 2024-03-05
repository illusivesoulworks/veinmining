package com.illusivesoulworks.veinmining;

import com.illusivesoulworks.spectrelib.config.SpectreLibInitializer;
import org.quiltmc.loader.api.ModContainer;

public class VeinMiningConfigInitializer implements SpectreLibInitializer {

  public void onInitializeConfig() {
    this.onInitializeConfig(null);
  }

  @Override
  public void onInitializeConfig(ModContainer modContainer) {
    VeinMiningMod.init();
  }
}
