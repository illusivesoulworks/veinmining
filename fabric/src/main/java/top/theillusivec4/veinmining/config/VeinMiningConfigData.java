package top.theillusivec4.veinmining.config;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;
import top.theillusivec4.veinmining.VeinMiningMod;

@Config(name = VeinMiningMod.MOD_ID)
public class VeinMiningConfigData implements ConfigData {

  @ConfigEntry.Gui.CollapsibleObject
  Enchantment enchantment = new Enchantment();

  @ConfigEntry.Gui.CollapsibleObject
  VeinMining veinMining = new VeinMining();

  public static class Enchantment {

    @ConfigEntry.Gui.Tooltip
    @Comment("The rarity of the enchantment")
    public net.minecraft.enchantment.Enchantment.Rarity rarity =
        net.minecraft.enchantment.Enchantment.Rarity.RARE;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 5)
    @Comment("The number of levels of the enchantment")
    public int levels = 1;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not to consider this enchantment as a treasure")
    public boolean isTreasure = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not this enchantment can be randomly selected for enchanting")
    public boolean isRandomlySelectable = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not this enchantment is available on books")
    public boolean isAvailableOnBooks = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    @Comment("The minimum enchanting power requirement for the first enchantment level")
    public int minPowerBase = 15;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    @Comment("The additional enchanting power requirement for each enchantment level after the first")
    public int minPowerPerLevel = 5;
  }

  public static class VeinMining {

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    @Comment("The maximum number of blocks to mine per level of the enchantment")
    public int maxBlocksPerLevel = 50;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    @Comment("The maximum distance from the source block per level of the enchantment")
    public int maxDistancePerLevel = 15;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not to vein mine diagonally, note this may lead to hidden drops if relocateDrops is false")
    public boolean diagonalMining = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not to move all drops to the same location")
    public boolean relocateDrops = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not the tool can break while mining additional blocks")
    public boolean preventToolDestruction = false;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not the tool takes damage from mining additional blocks")
    public boolean addToolDamage = true;

    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 1, max = 1000)
    @Comment("The multiplier to tool damage from mining additional blocks")
    public int toolDamageMultiplier = 1;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not the player gets exhaustion from mining additional blocks")
    public boolean addPlayerExhaustion = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("The multiplier to player exhaustion from mining additional blocks")
    public double playerExhaustionMultiplier = 1.0D;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether or not to stop vein mining when the tool can no longer be used")
    public boolean limitedByDurability = true;

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether to activate vein mining by standing or crouching")
    public VeinMiningConfig.ActivationState activationState =
        VeinMiningConfig.ActivationState.STANDING;

    @ConfigEntry.Gui.Tooltip
    @Comment("List of whitelisted/blacklisted blocks or block tags")
    public List<String> blocks = new ArrayList<>();

    @ConfigEntry.Gui.Tooltip
    @Comment("Whether the blocks configuration is a whitelist or a blacklist")
    public VeinMiningConfig.PermissionType blocksPermission =
        VeinMiningConfig.PermissionType.BLACKLIST;

    @ConfigEntry.Gui.Tooltip
    @Comment("List of groupings by block IDs or block tags, comma-separated")
    public List<String> groups = Lists.newArrayList(
        "#c:adamantite_ores",
        "#c:aetherium_ores",
        "#c:aluminum_ores",
        "#c:amethyst_ores",
        "#c:antimony_ores",
        "#c:aquarium_ores",
        "#c:asterite_ores",
        "#c:banglum_ores",
        "#c:bauxite_ores",
        "#c:carmot_ores",
        "#c:certus_quartz_ores",
        "#c:cinnabar_ores",
        "#c:coal_ores",
        "#c:cobalt_ores",
        "#c:copper_ores",
        "#c:diamond_ores",
        "#c:emerald_ores",
        "#c:galaxium_ores",
        "#c:galena_ores",
        "#c:gold_ores,#minecraft:gold_ores",
        "#c:iridium_ores",
        "#c:iron_ores",
        "#c:kyber_ores",
        "#c:lapis_ores",
        "#c:lead_ores",
        "#c:lunum_ores",
        "#c:lutetium_ores",
        "#c:manganese_ores",
        "#c:metite_ores",
        "#c:mythril_ores",
        "#c:nickel_ores",
        "#c:orichalcum_ores",
        "#c:osmium_ores",
        "#c:palladium_ores",
        "#c:peridot_ores",
        "#c:platinum_ores",
        "#c:prometheum_ores",
        "#c:pyrite_ores",
        "#c:quadrillum_ores",
        "#c:quartz_ores",
        "#c:redstone_ores",
        "#c:ruby_ores",
        "#c:runite_ores",
        "#c:salt_ores",
        "#c:sapphire_ores",
        "#c:sheldonite_ores",
        "#c:silver_ores",
        "#c:sodalite_ores",
        "#c:sphalerite_ores",
        "#c:starrite_ores",
        "#c:stellum_ores",
        "#c:stormyx_ores",
        "#c:sulfur_ores",
        "#c:tantalite_ores",
        "#c:tin_ores",
        "#c:titanium_ores",
        "#c:topaz_ores",
        "#c:truesilver_ores",
        "#c:tungsten_ores",
        "#c:unobtainium_ores",
        "#c:ur_ores",
        "#c:uranium_ores",
        "#c:vermiculite_ores",
        "#c:zinc_ores"
    );
  }
}
