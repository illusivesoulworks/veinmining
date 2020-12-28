package top.theillusivec4.veinmining.veinmining;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class VeinMiningEnchantment extends Enchantment {

  public VeinMiningEnchantment() {
    super(Rarity.RARE, EnchantmentTarget.DIGGER, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
  }

  @Override
  public Rarity getRarity() {
    return VeinMiningConfig.Enchantment.rarity;
  }

  @Override
  public int getMaxLevel() {
    return VeinMiningConfig.Enchantment.levels;
  }

  @Override
  public int getMinPower(int level) {
    return VeinMiningConfig.Enchantment.minPowerBase +
        (level - 1) * VeinMiningConfig.Enchantment.minPowerPerLevel;
  }

  @Override
  public int getMaxPower(int level) {
    return super.getMinPower(level) + 50;
  }

  @Override
  public boolean isTreasure() {
    return VeinMiningConfig.Enchantment.isTreasure;
  }

  @Override
  public boolean isAvailableForEnchantedBookOffer() {
    return VeinMiningConfig.Enchantment.isAvailableOnBooks;
  }

  @Override
  public boolean isAvailableForRandomSelection() {
    return VeinMiningConfig.Enchantment.isRandomlySelectable;
  }
}
