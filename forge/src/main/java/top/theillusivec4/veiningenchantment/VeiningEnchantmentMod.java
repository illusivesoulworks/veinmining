package top.theillusivec4.veiningenchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.veiningenchantment.config.VeiningEnchantmentConfig;
import top.theillusivec4.veiningenchantment.veining.VeiningEnchantment;

@Mod(VeiningEnchantmentMod.MOD_ID)
public class VeiningEnchantmentMod {

  public static final String MOD_ID = "veiningenchantment";
  public static final Enchantment VEINING = new VeiningEnchantment();

  public VeiningEnchantmentMod() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::config);
    ModLoadingContext.get()
        .registerConfig(ModConfig.Type.SERVER, VeiningEnchantmentConfig.CONFIG_SPEC);
  }

  private void config(final ModConfig.ModConfigEvent evt) {

    if (evt.getConfig().getModId().equals(MOD_ID)) {
      VeiningEnchantmentConfig.bake();
    }
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> evt) {
      evt.getRegistry().register(VEINING);
    }
  }
}
