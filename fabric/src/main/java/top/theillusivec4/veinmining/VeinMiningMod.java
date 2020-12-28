package top.theillusivec4.veinmining;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.veinmining.config.VeinMiningConfig;
import top.theillusivec4.veinmining.config.VeinMiningConfigData;
import top.theillusivec4.veinmining.veinmining.VeinMiningEnchantment;
import top.theillusivec4.veinmining.veinmining.logic.BlockProcessor;

public class VeinMiningMod implements ModInitializer {

  public static final String MOD_ID = "veinmining";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final Enchantment VEIN_MINING = new VeinMiningEnchantment();
  public static VeinMiningConfigData configData;

  @Override
  public void onInitialize() {
    Registry.register(Registry.ENCHANTMENT, new Identifier(MOD_ID, "vein_mining"), VEIN_MINING);
    configData =
        AutoConfig.register(VeinMiningConfigData.class, JanksonConfigSerializer::new).getConfig();
    ServerLifecycleEvents.SERVER_STARTED.register(minecraftServer -> {
      VeinMiningConfig.bake(configData);
      BlockProcessor.rebuild();
    });
    ServerLifecycleEvents.END_DATA_PACK_RELOAD
        .register((minecraftServer, serverResourceManager, b) -> {
          VeinMiningConfig.bake(configData);
          BlockProcessor.rebuild();
        });
  }
}
