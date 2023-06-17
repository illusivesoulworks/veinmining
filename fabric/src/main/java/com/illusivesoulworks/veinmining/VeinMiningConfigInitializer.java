package com.illusivesoulworks.veinmining;

import com.illusivesoulworks.spectrelib.config.SpectreLibInitializer;

public class VeinMiningConfigInitializer implements SpectreLibInitializer {

  @Override
  public void onInitializeConfig() {
    VeinMiningMod.init();
  }
}
