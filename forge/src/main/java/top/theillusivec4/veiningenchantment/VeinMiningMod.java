package top.theillusivec4.veiningenchantment;

import javax.annotation.Nonnull;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.veiningenchantment.config.VeinMiningConfig;
import top.theillusivec4.veiningenchantment.veinmining.VeinMiningEnchantment;
import top.theillusivec4.veiningenchantment.veinmining.logic.BlockProcessor;

@Mod(VeinMiningMod.MOD_ID)
public class VeinMiningMod {

  public static final String MOD_ID = "veinmining";
  public static final Logger LOGGER = LogManager.getLogger();
  public static final Enchantment VEIN_MINING = new VeinMiningEnchantment();

  public VeinMiningMod() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addGenericListener(Enchantment.class, this::registerEnchantment);
    eventBus.addListener(this::config);
    MinecraftForge.EVENT_BUS.addListener(this::reload);
    ModLoadingContext.get()
        .registerConfig(ModConfig.Type.SERVER, VeinMiningConfig.CONFIG_SPEC);
  }

  private void registerEnchantment(final RegistryEvent.Register<Enchantment> evt) {
    evt.getRegistry().register(VEIN_MINING);
  }

  private void config(final ModConfig.ModConfigEvent evt) {

    if (evt.getConfig().getModId().equals(MOD_ID)) {
      VeinMiningConfig.bake();
      BlockProcessor.rebuild();
    }
  }

  private void reload(final AddReloadListenerEvent evt) {
    evt.addListener(new ReloadListener<Void>() {

      @Nonnull
      @Override
      protected Void prepare(@Nonnull IResourceManager resourceManagerIn,
                             @Nonnull IProfiler profilerIn) {
        return null;
      }

      @Override
      protected void apply(@Nonnull Void objectIn, @Nonnull IResourceManager resourceManagerIn,
                           @Nonnull IProfiler profilerIn) {
        BlockProcessor.rebuild();
      }
    });
  }
}
