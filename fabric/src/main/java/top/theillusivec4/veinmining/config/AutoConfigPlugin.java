package top.theillusivec4.veinmining.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;

public class AutoConfigPlugin {

  private static VeinMiningConfigData configData;

  public static void init() {
    configData =
        AutoConfig.register(VeinMiningConfigData.class, JanksonConfigSerializer::new).getConfig();
    readConfigData();
    AutoConfig.getConfigHolder(VeinMiningConfigData.class)
        .registerSaveListener((configHolder, veinMiningConfigData) -> {
          readConfigData();
          return ActionResult.PASS;
        });
  }

  public static void readConfigData() {
    VeinMiningConfig.VeinMining.bake(configData);
    VeinMiningConfig.Enchantment.bake(configData);
  }

  public static Screen getConfigScreen(Screen screen) {
    return AutoConfig.getConfigScreen(VeinMiningConfigData.class, screen).get();
  }
}
