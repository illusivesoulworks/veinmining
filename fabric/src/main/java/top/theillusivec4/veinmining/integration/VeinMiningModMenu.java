package top.theillusivec4.veinmining.integration;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import top.theillusivec4.veinmining.config.VeinMiningConfigData;

public class VeinMiningModMenu implements ModMenuApi {

  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return screen -> AutoConfig.getConfigScreen(VeinMiningConfigData.class, screen).get();
  }
}
