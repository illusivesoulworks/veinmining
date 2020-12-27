package top.theillusivec4.veiningenchantment;

import java.util.concurrent.CompletableFuture;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.veiningenchantment.config.VeiningEnchantmentConfig;
import top.theillusivec4.veiningenchantment.veining.VeiningEnchantment;
import top.theillusivec4.veiningenchantment.veining.logic.BlockChecker;

@Mod(VeiningEnchantmentMod.MOD_ID)
public class VeiningEnchantmentMod {

  public static final String MOD_ID = "veiningenchantment";
  public static final Enchantment VEINING = new VeiningEnchantment();

  public VeiningEnchantmentMod() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::registerEnchantment);
    eventBus.addListener(this::config);
    MinecraftForge.EVENT_BUS.addListener(this::reload);
    ModLoadingContext.get()
        .registerConfig(ModConfig.Type.SERVER, VeiningEnchantmentConfig.CONFIG_SPEC);
  }

  private void registerEnchantment(final RegistryEvent.Register<Enchantment> evt) {
    evt.getRegistry().register(VEINING);
  }

  private void config(final ModConfig.ModConfigEvent evt) {

    if (evt.getConfig().getModId().equals(MOD_ID)) {
      VeiningEnchantmentConfig.bake();
      BlockChecker.reset();
    }
  }

  private void reload(final AddReloadListenerEvent evt) {
    evt.addListener(
        (stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) -> CompletableFuture
            .runAsync(BlockChecker::reset));
  }
}
