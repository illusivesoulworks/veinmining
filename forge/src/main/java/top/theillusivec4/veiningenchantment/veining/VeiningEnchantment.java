package top.theillusivec4.veiningenchantment.veining;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import top.theillusivec4.veiningenchantment.VeiningEnchantmentMod;

public class VeiningEnchantment extends Enchantment {

  private static final String ID = VeiningEnchantmentMod.MOD_ID + ":veining";

  public VeiningEnchantment() {
    super(Rarity.RARE, EnchantmentType.DIGGER,
        new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    this.setRegistryName(ID);
  }
}
