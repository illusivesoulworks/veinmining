package top.theillusivec4.veinmining.config;

import java.util.HashSet;
import java.util.Set;

public class VeinMiningConfig {

  public static void bake(VeinMiningConfigData configData) {
    Enchantment.bake(configData);
    VeinMining.bake(configData);
  }

  public static class Enchantment {

    public static net.minecraft.enchantment.Enchantment.Rarity rarity =
        net.minecraft.enchantment.Enchantment.Rarity.RARE;
    public static int levels = 1;
    public static boolean isTreasure = false;
    public static boolean isRandomlySelectable = true;
    public static boolean isAvailableOnBooks = true;
    public static int minPowerBase = 15;
    public static int minPowerPerLevel = 5;

    public static void bake(VeinMiningConfigData configData) {
      VeinMiningConfigData.Enchantment enchantment = configData.enchantment;
      rarity = enchantment.rarity;
      levels = enchantment.levels;
      isTreasure = enchantment.isTreasure;
      isRandomlySelectable = enchantment.isRandomlySelectable;
      isAvailableOnBooks = enchantment.isAvailableOnBooks;
      minPowerBase = enchantment.minPowerBase;
      minPowerPerLevel = enchantment.minPowerPerLevel;
    }
  }

  public static class VeinMining {
    public static int maxBlocksPerLevel = 50;
    public static int maxDistancePerLevel = 15;
    public static boolean diagonalMining = true;
    public static boolean relocateDrops = true;
    public static boolean preventToolDestruction = false;
    public static boolean addToolDamage = true;
    public static int toolDamageMultiplier = 1;
    public static boolean addPlayerExhaustion = true;
    public static double playerExhaustionMultiplier = 1.0D;
    public static boolean limitedByDurability = true;
    public static ActivationState activationState = ActivationState.STANDING;
    public static Set<String> blocks = new HashSet<>();
    public static PermissionType blocksPermission = PermissionType.BLACKLIST;
    public static Set<String> groups = new HashSet<>();

    public static void bake(VeinMiningConfigData configData) {
      VeinMiningConfigData.VeinMining veinMining = configData.veinMining;
      maxBlocksPerLevel = veinMining.maxBlocksPerLevel;
      maxDistancePerLevel = veinMining.maxDistancePerLevel;
      diagonalMining = veinMining.diagonalMining;
      relocateDrops = veinMining.relocateDrops;
      preventToolDestruction = veinMining.preventToolDestruction;
      addToolDamage = veinMining.addToolDamage;
      toolDamageMultiplier = veinMining.toolDamageMultiplier;
      addPlayerExhaustion = veinMining.addPlayerExhaustion;
      playerExhaustionMultiplier = veinMining.playerExhaustionMultiplier;
      limitedByDurability = veinMining.limitedByDurability;
      activationState = veinMining.activationState;
      blocks = new HashSet<>();
      blocks.addAll(veinMining.blocks);
      blocksPermission = veinMining.blocksPermission;
      groups = new HashSet<>();
      groups.addAll(veinMining.groups);
    }
  }

  public enum PermissionType {
    BLACKLIST,
    WHITELIST
  }

  public enum ActivationState {
    STANDING,
    CROUCHING
  }
}
