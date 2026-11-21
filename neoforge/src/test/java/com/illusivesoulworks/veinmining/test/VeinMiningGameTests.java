package com.illusivesoulworks.veinmining.test;

import com.illusivesoulworks.veinmining.VeinMiningConstants;
import com.illusivesoulworks.veinmining.common.veinmining.VeinMiningPlayers;
import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Dev-only game tests that exercise the server-side vein mining path end to end.
 * Run with: ./gradlew :neoforge:runGameTestServer
 */
@EventBusSubscriber(modid = VeinMiningConstants.MOD_ID)
public class VeinMiningGameTests {

  private static final Identifier VEIN_MINE_ORES =
      Identifier.fromNamespaceAndPath(VeinMiningConstants.MOD_ID, "vein_mine_ores");
  private static final Identifier EMPTY_STRUCTURE =
      Identifier.fromNamespaceAndPath(VeinMiningConstants.MOD_ID, "empty");

  @SubscribeEvent
  public static void registerFunctions(final RegisterEvent evt) {
    evt.register(Registries.TEST_FUNCTION, VEIN_MINE_ORES,
        () -> (Consumer<GameTestHelper>) VeinMiningGameTests::veinMineOres);
  }

  @SubscribeEvent
  public static void registerTests(final RegisterGameTestsEvent evt) {
    Holder<TestEnvironmentDefinition<?>> env = evt.registerEnvironment(
        Identifier.fromNamespaceAndPath(VeinMiningConstants.MOD_ID, "default"));
    evt.registerTest(VEIN_MINE_ORES, new FunctionGameTestInstance(
        ResourceKey.create(Registries.TEST_FUNCTION, VEIN_MINE_ORES),
        new TestData<>(env, EMPTY_STRUCTURE, 100, 0, true)));
  }

  // A 5-block iron vein (4 in a row plus one diagonal) with an adjacent stone block that must
  // survive. Breaking the first ore with a Vein Mining pickaxe should take the whole vein,
  // relocate drops to the origin, and damage the tool once per block.
  private static void veinMineOres(GameTestHelper helper) {
    List<BlockPos> vein = List.of(new BlockPos(1, 1, 1), new BlockPos(2, 1, 1),
        new BlockPos(3, 1, 1), new BlockPos(3, 2, 1), new BlockPos(4, 3, 2));
    BlockPos stone = new BlockPos(1, 2, 1);
    vein.forEach(pos -> helper.setBlock(pos, Blocks.IRON_ORE));
    helper.setBlock(stone, Blocks.STONE);

    var level = helper.getLevel();
    FakePlayer player =
        FakePlayerFactory.get(level, new GameProfile(UUID.randomUUID(), "veinmining_test"));
    BlockPos origin = helper.absolutePos(vein.get(0));
    player.setPos(origin.getX() + 0.5, origin.getY() + 1, origin.getZ() - 1.5);

    Holder<Enchantment> veinMining = level.registryAccess()
        .lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(VeinMiningConstants.ENCHANTMENT);
    ItemStack pickaxe = new ItemStack(Items.DIAMOND_PICKAXE);
    pickaxe.enchant(veinMining, 1);
    player.setItemInHand(InteractionHand.MAIN_HAND, pickaxe);

    VeinMiningPlayers.activateVeinMining(player, level.getGameTime());
    boolean broke = player.gameMode.destroyBlock(origin);
    VeinMiningPlayers.deactivateVeinMining(player);

    if (!broke) {
      helper.fail("destroyBlock returned false for the origin ore");
      return;
    }
    vein.forEach(pos -> helper.assertBlockPresent(Blocks.AIR, pos));
    helper.assertBlockPresent(Blocks.STONE, stone);

    int damage = player.getMainHandItem().getDamageValue();

    if (damage != vein.size()) {
      helper.fail("Expected tool damage " + vein.size() + " but was " + damage);
      return;
    }
    // relocateDrops is on by default, so every raw iron should land at the origin block
    int nearOrigin = level.getEntitiesOfClass(ItemEntity.class, new AABB(origin).inflate(1.0))
        .stream().filter(e -> e.getItem().is(Items.RAW_IRON))
        .mapToInt(e -> e.getItem().getCount()).sum();

    if (nearOrigin < vein.size()) {
      helper.fail("Expected at least " + vein.size() + " raw iron at the origin but found "
          + nearOrigin);
      return;
    }
    helper.succeed();
  }
}
