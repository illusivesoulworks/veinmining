package top.theillusivec4.veinmining.veinmining;

import javax.annotation.Nonnull;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import top.theillusivec4.veinmining.VeinMiningMod;
import top.theillusivec4.veinmining.config.VeinMiningConfig;

public class VeinMiningEnchantment extends Enchantment {

  private static final String ID = VeinMiningMod.MOD_ID + ":vein_mining";

  public VeinMiningEnchantment() {
    super(Rarity.RARE, EnchantmentType.DIGGER,
        new EquipmentSlotType[] {EquipmentSlotType.MAINHAND});
    this.setRegistryName(ID);
  }

  @Nonnull
  @Override
  public Rarity getRarity() {
    return VeinMiningConfig.Enchantment.rarity;
  }

  @Override
  public int getMaxLevel() {
    return VeinMiningConfig.Enchantment.levels;
  }

  @Override
  public int getMinEnchantability(int enchantmentLevel) {
    return VeinMiningConfig.Enchantment.minEnchantabilityBase +
        VeinMiningConfig.Enchantment.minEnchantabilityPerLevel * (enchantmentLevel - 1);
  }

  @Override
  public int getMaxEnchantability(int enchantmentLevel) {
    return this.getMinEnchantability(enchantmentLevel) + 50;
  }

  @Override
  public boolean isTreasureEnchantment() {
    return VeinMiningConfig.Enchantment.isTreasure;
  }

  @Override
  public boolean canVillagerTrade() {
    return VeinMiningConfig.Enchantment.isVillagerTrade;
  }

  @Override
  public boolean canGenerateInLoot() {
    return VeinMiningConfig.Enchantment.isLootable;
  }

  @Override
  public boolean canApplyAtEnchantingTable(@Nonnull ItemStack stack) {
    return VeinMiningConfig.Enchantment.canApplyAtEnchantingTable;
  }

  @Override
  public boolean isAllowedOnBooks() {
    return VeinMiningConfig.Enchantment.canApplyOnBooks;
  }
}
