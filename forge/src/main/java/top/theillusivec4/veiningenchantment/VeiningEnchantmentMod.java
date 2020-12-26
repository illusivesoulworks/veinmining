package top.theillusivec4.veiningenchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.veiningenchantment.veining.VeiningEnchantment;

@Mod(VeiningEnchantmentMod.MOD_ID)
public class VeiningEnchantmentMod {

  public static final String MOD_ID = "veiningenchantment";
  public static final Enchantment VEINING = new VeiningEnchantment();

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class RegistryEvents {

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> evt) {
      evt.getRegistry().register(VEINING);
    }
  }
}
