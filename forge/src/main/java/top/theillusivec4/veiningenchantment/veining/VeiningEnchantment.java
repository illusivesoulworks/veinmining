package top.theillusivec4.veiningenchantment.veining;

import javax.annotation.Nonnull;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import top.theillusivec4.veiningenchantment.VeiningEnchantmentMod;
import top.theillusivec4.veiningenchantment.config.VeiningEnchantmentConfig;

public class VeiningEnchantment extends Enchantment {

  private static final String ID = VeiningEnchantmentMod.MOD_ID + ":veining";

  public VeiningEnchantment() {
    super(Rarity.RARE, EnchantmentType.DIGGER,
        new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    this.setRegistryName(ID);
  }

  @Nonnull
  @Override
  public Rarity getRarity() {
    return VeiningEnchantmentConfig.Enchantment.rarity;
  }

  @Override
  public int getMaxLevel() {
    return VeiningEnchantmentConfig.Enchantment.levels;
  }

  @Override
  public int getMinEnchantability(int enchantmentLevel) {
    return 1 + 10 * (enchantmentLevel - 1);
  }

  @Override
  public int getMaxEnchantability(int enchantmentLevel) {
    return this.getMinEnchantability(enchantmentLevel) + 50;
  }

  @Override
  public boolean isTreasureEnchantment() {
    return VeiningEnchantmentConfig.Enchantment.isTreasure;
  }

  @Override
  public boolean canVillagerTrade() {
    return VeiningEnchantmentConfig.Enchantment.isVillagerTrade;
  }

  @Override
  public boolean canGenerateInLoot() {
    return VeiningEnchantmentConfig.Enchantment.isLootable;
  }

  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    return VeiningEnchantmentConfig.Enchantment.canApplyAtEnchantingTable;
  }

  @Override
  public boolean isAllowedOnBooks() {
    return VeiningEnchantmentConfig.Enchantment.canApplyOnBooks;
  }
}
