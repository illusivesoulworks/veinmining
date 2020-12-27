package top.theillusivec4.veiningenchantment.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;
import top.theillusivec4.veiningenchantment.VeiningEnchantmentMod;

public class VeiningEnchantmentConfig {

  public static final ForgeConfigSpec CONFIG_SPEC;
  public static final Config CONFIG;
  private static final String CONFIG_PREFIX = "gui." + VeiningEnchantmentMod.MOD_ID + ".config.";

  static {
    final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
        .configure(Config::new);
    CONFIG_SPEC = specPair.getRight();
    CONFIG = specPair.getLeft();
  }

  public static void bake() {
    Enchantment.bake();
    Veining.bake();
  }

  public static class Enchantment {
    public static net.minecraft.enchantment.Enchantment.Rarity rarity;
    public static int levels;
    public static boolean isTreasure;
    public static boolean isVillagerTrade;
    public static boolean isLootable;
    public static boolean canApplyAtEnchantingTable;
    public static boolean canApplyOnBooks;

    public static void bake() {
      rarity = CONFIG.rarity.get();
      levels = CONFIG.levels.get();
      isTreasure = CONFIG.isTreasure.get();
      isVillagerTrade = CONFIG.isVillagerTrade.get();
      isLootable = CONFIG.isLootable.get();
      canApplyAtEnchantingTable = CONFIG.canApplyAtEnchantingTable.get();
      canApplyOnBooks = CONFIG.canApplyOnBooks.get();
    }
  }

  public static class Veining {

    public static int maxBlocksPerLevel;
    public static int maxDistancePerLevel;
    public static boolean diagonalMining;
    public static boolean relocateDrops;
    public static boolean preventToolDestruction;
    public static boolean addToolDamage;
    public static int toolDamageMultiplier;
    public static boolean addPlayerExhaustion;
    public static double playerExhaustionMultiplier;

    public static void bake() {
      maxBlocksPerLevel = CONFIG.maxBlocksPerLevel.get();
      maxDistancePerLevel = CONFIG.maxDistancePerLevel.get();
      diagonalMining = CONFIG.diagonalMining.get();
      relocateDrops = CONFIG.relocateDrops.get();
      preventToolDestruction = CONFIG.preventToolDestruction.get();
      addToolDamage = CONFIG.addToolDamage.get();
      toolDamageMultiplier = CONFIG.toolDamageMultiplier.get();
      addPlayerExhaustion = CONFIG.addPlayerExhaustion.get();
      playerExhaustionMultiplier = CONFIG.playerExhaustionMultiplier.get();
    }
  }

  public static class Config {

    public final EnumValue<net.minecraft.enchantment.Enchantment.Rarity> rarity;
    public final IntValue levels;
    public final BooleanValue isTreasure;
    public final BooleanValue isVillagerTrade;
    public final BooleanValue isLootable;
    public final BooleanValue canApplyAtEnchantingTable;
    public final BooleanValue canApplyOnBooks;

    public final IntValue maxBlocksPerLevel;
    public final IntValue maxDistancePerLevel;
    public final BooleanValue diagonalMining;
    public final BooleanValue relocateDrops;
    public final BooleanValue preventToolDestruction;
    public final BooleanValue addToolDamage;
    public final IntValue toolDamageMultiplier;
    public final BooleanValue addPlayerExhaustion;
    public final DoubleValue playerExhaustionMultiplier;

    public Config(ForgeConfigSpec.Builder builder) {
      builder.push("enchantment");

      rarity = builder.comment("The rarity of the enchantment")
          .translation(CONFIG_PREFIX + "rarity")
          .defineEnum("rarity", net.minecraft.enchantment.Enchantment.Rarity.RARE);

      levels = builder.comment("The number of levels of the enchantment")
          .translation(CONFIG_PREFIX + "levels")
          .defineInRange("levels", 1, 1, 5);

      isTreasure = builder.comment("Whether or not to consider this enchantment as a treasure")
          .translation(CONFIG_PREFIX + "isTreasure")
          .define("isTreasure", false);

      isVillagerTrade =
          builder.comment("Whether or not this enchantment can be offered by villagers for trade")
              .translation(CONFIG_PREFIX + "isVillagerTrade")
              .define("isVillagerTrade", true);

      isLootable = builder.comment("Whether or not this enchantment can generate in loot")
          .translation(CONFIG_PREFIX + "isLootable")
          .define("isLootable", true);

      canApplyAtEnchantingTable =
          builder.comment("Whether or not this enchantment can be applied at the enchanting table")
              .translation(CONFIG_PREFIX + "canApplyAtEnchantingTable")
              .define("canApplyAtEnchantingTable", true);

      canApplyOnBooks = builder.comment("Whether or not this enchantment can be applied on books")
          .translation(CONFIG_PREFIX + "canApplyOnBooks")
          .define("canApplyOnBooks", true);

      builder.pop();

      builder.push("veining");

      maxBlocksPerLevel =
          builder.comment("The maximum number of blocks to mine per level of the enchantment")
              .translation(CONFIG_PREFIX + "maxBlocksPerLevel")
              .defineInRange("maxBlocksPerLevel", 50, 1, 1000);

      maxDistancePerLevel =
          builder.comment("The maximum distance from the source block per level of the enchantment")
              .translation(CONFIG_PREFIX + "maxDistancePerLevel")
              .defineInRange("maxDistancePerLevel", 10, 1, 100);

      diagonalMining =
          builder.comment("Whether or not to mine diagonally, note this may lead to hidden drops")
              .translation(CONFIG_PREFIX + "diagonalMining")
              .define("diagonalMining", false);

      relocateDrops = builder.comment("Whether or not to move the drops from veining to the same location as the original block")
          .translation(CONFIG_PREFIX + "relocateDrops")
          .define("relocateDrops", true);

      preventToolDestruction = builder.comment("Whether or not to stop veining before the tool breaks")
          .translation(CONFIG_PREFIX + "preventToolDestruction")
          .define("preventToolDestruction", false);

      addToolDamage =
          builder.comment("Whether or not the tool takes additional damage from veining")
              .translation(CONFIG_PREFIX + "addToolDamage")
              .define("addToolDamage", true);

      toolDamageMultiplier =
          builder.comment("The multiplier to additional tool damage from veining")
              .translation(CONFIG_PREFIX + "toolDamageMultiplier")
              .defineInRange("toolDamageMultiplier", 1, 0, 1000);

      addPlayerExhaustion =
          builder.comment("Whether or not the player gets additional exhaustion from veining")
              .translation(CONFIG_PREFIX + "addPlayerExhaustion")
              .define("addPlayerExhaustion", true);

      playerExhaustionMultiplier =
          builder.comment("The multiplier to additional player exhaustion from veining")
              .translation(CONFIG_PREFIX + "playerExhaustionMultiplier")
              .defineInRange("playerExhaustionMultiplier", 1.0F, 0.0F, 1000.0F);

      builder.pop();
    }
  }
}
