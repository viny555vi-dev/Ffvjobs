package com.ffvreborn.content;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import com.ffvreborn.abilities.AbilityDefinition;
import com.ffvreborn.abilities.AbilityService;
import com.ffvreborn.core.JobDefinition;
import com.ffvreborn.core.JobService;
import com.ffvreborn.core.PlayerProgression;
import com.ffvreborn.core.PlayerRpgData;
public final class FfvGameTests {
    private FfvGameTests() {}
    public static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> emptyEnvironment = event.registerEnvironment(
            Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, "empty"),
            new TestEnvironmentDefinition.AllOf()
        );
        TestData<Holder<TestEnvironmentDefinition<?>>> data =
            new TestData<>(emptyEnvironment, Identifier.fromNamespaceAndPath("minecraft", "empty"), 200, 0, true);
        register(event, "item_registered", FinalFantasyVReborn.ITEM_TEST, data);
        register(event, "job_progress", FinalFantasyVReborn.JOB_TEST, data);
        register(event, "ability_cast", FinalFantasyVReborn.ABILITY_TEST, data);
        register(event, "summon_cast", FinalFantasyVReborn.SUMMON_TEST, data);
        register(event, "combat_progression", FinalFantasyVReborn.PROGRESSION_TEST, data);
        register(event, "bestiary_spawn", FinalFantasyVReborn.BESTIARY_TEST, data);
        register(event, "loot_balance", FinalFantasyVReborn.LOOT_TEST, data);
        register(event, "ability_progression", FinalFantasyVReborn.ABILITY_PROGRESS_TEST, data);
        register(event, "summon_entity", FinalFantasyVReborn.SUMMON_ENTITY_TEST, data);
        register(event, "controller_mapping", FinalFantasyVReborn.GAMEPAD_TEST, data);
    }
    private static void register(RegisterGameTestsEvent event, String id,
        net.neoforged.neoforge.registries.DeferredHolder<java.util.function.Consumer<GameTestHelper>, java.util.function.Consumer<GameTestHelper>> function,
        TestData<Holder<TestEnvironmentDefinition<?>>> data) {
        event.registerTest(Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, id),
            new FunctionGameTestInstance(function.getKey(), data));
    }
    public static void itemRegistered(GameTestHelper helper) {
        helper.assertTrue(ModItems.CRYSTAL_OF_DAWN.isBound(), "Crystal of Dawn was not registered");
        helper.assertTrue(ModItems.EMBERSTEEL_BLADE.isBound(), "Embersteel Blade was not registered");
        helper.assertTrue(ModItems.VERDANT_LONGBOW.get() instanceof net.minecraft.world.item.BowItem, "Verdant Longbow is not a functional bow item");
        helper.assertTrue(ModItems.AETHER_SHARD.isBound(), "Aether Shard was not registered");
        helper.assertTrue(ModBlocks.AETHER_PRISM.isBound(), "Aether Prism particle material was not registered");
        helper.assertTrue(ModBlocks.FROST_PRISM.isBound(), "Frost Prism particle material was not registered");
        helper.assertTrue(ModBlocks.RUNE_PRISM.isBound(), "Rune Prism particle material was not registered");
        helper.succeed();
    }
    public static void jobProgressWorks(GameTestHelper helper) {
        PlayerRpgData player = new PlayerRpgData(UUID.randomUUID());
        JobService jobs = new JobService(List.of(
            new JobDefinition("freelancer", "Freelancer", "Generalist", Set.of()),
            new JobDefinition("knight", "Knight", "Tank", Set.of())
        ));
        jobs.changeJob(player, "knight");
        jobs.grantActionXp(player, 500);
        helper.assertTrue(player.progressFor("knight").level() > 1, "Knight did not gain a level");
        helper.assertTrue(player.progressFor("freelancer").level() == 1, "Job XP leaked into Freelancer");
        helper.succeed();
    }
    public static void abilityCanBeLearnedAndCast(GameTestHelper helper) {
        PlayerRpgData player = new PlayerRpgData(UUID.randomUUID());
        player.setActiveJob("black_mage");
        player.progressFor("black_mage").addAbp(20);
        AbilityDefinition fire = new AbilityDefinition("fire", "black_mage", 1, 10, 15, false, 20, "fire_damage");
        AbilityService abilities = new AbilityService(List.of(fire));
        abilities.learn(player, "fire");
        abilities.equip(player, "fire", AbilityService.SlotType.ACTIVE, 0);
        AbilityService.CastResult result = abilities.cast(player, "fire", 100);
        helper.assertTrue(result.success(), "Fire could not be cast: " + result.reason());
        helper.assertTrue(player.mana() == 85, "Casting Fire did not consume 15 mana");
        helper.succeed();
    }
    public static void summonCanBeInvoked(GameTestHelper helper) {
        PlayerRpgData player = new PlayerRpgData(UUID.randomUUID());
        player.setActiveJob("summoner");
        SummonService summons = new SummonService(List.of(new SummonService.Summon("ifrit", 25, 100, 1, "fire_burst")));
        String effect = summons.invoke(player, "ifrit", 10);
        helper.assertTrue("fire_burst".equals(effect), "Ifrit returned the wrong effect");
        helper.assertTrue(player.mana() == 75, "Ifrit did not consume 25 mana");
        helper.succeed();
    }
    public static void combatProgression(GameTestHelper helper) {
        PlayerProgression player = PlayerProgression.fresh().withJob("knight").awardKillXp(135);
        helper.assertTrue(player.level("knight") == 2, "Kill XP did not level the active job");
        helper.assertTrue(player.level("freelancer") == 1, "Kill XP leaked to another job");
        helper.assertTrue(player.abp("knight") == 2, "Level-up did not award ABP");
        helper.assertTrue(CombatLoot.xpForKill(24, true) == 60, "Mob XP formula returned an unexpected value");
        helper.succeed();
    }
    public static void bestiarySpawn(GameTestHelper helper) {
        helper.assertTrue(ModEntities.CINDER_WOLF.isBound(), "Cinder Wolf type is missing");
        helper.assertTrue(ModEntities.MIRE_HAG.isBound(), "Mire Hag type is missing");
        helper.assertTrue(ModEntities.AETHER_WISP.isBound(), "Aether Wisp type is missing");
        var mob = helper.spawn(ModEntities.CINDER_WOLF.get(), new BlockPos(2, 1, 2));
        helper.assertTrue(mob.isAlive(), "Cinder Wolf failed to spawn in a live test world");
        helper.succeed();
    }

    public static void abilityProgression(GameTestHelper helper) {
        PlayerProgression player = PlayerProgression.fresh().withJob("black_mage").awardKillXp(375);
        helper.assertTrue(player.level("black_mage") >= 3, "Black Mage did not level from combat XP");
        player = player.learnAbility("fire", 1, 2);
        helper.assertTrue(player.hasAbility("fire"), "Ability was not persisted as learned");
        helper.assertTrue(player.abp("black_mage") >= 2, "Learning did not spend ABP");
        player = player.spendMana(12);
        helper.assertTrue(player.mana() == 88, "Spell did not consume mana");
        helper.succeed();
    }

    public static void controllerMapping(GameTestHelper helper) {
        GamepadMapping.State input = GamepadMapping.map(0.8f, -0.8f, -0.5f, 0.4f,
            0.7f, 0.8f, true, false, false, true, true);
        helper.assertTrue(input.up() && input.right(), "Left stick directions were not mapped");
        helper.assertTrue(input.jump() && input.attack() && input.use() && input.toggleCamera(),
            "DualSense buttons and triggers were not mapped");
        helper.assertTrue(Math.abs(input.yawDelta() - 1.6f) < 0.001f, "Right stick yaw mapping is incorrect");
        helper.assertTrue(Math.abs(input.pitchDelta() - 1.08f) < 0.001f, "Right stick pitch mapping is incorrect");
        GamepadMapping.State neutral = GamepadMapping.map(0.05f, -0.05f, 0.04f, -0.04f,
            0.0f, 0.0f, false, false, false, false, false);
        helper.assertTrue(!neutral.up() && !neutral.down() && !neutral.left() && !neutral.right(),
            "Controller deadzone allowed stick drift");
        helper.assertTrue(neutral.yawDelta() == 0.0f && neutral.pitchDelta() == 0.0f,
            "Controller look deadzone allowed camera drift");
        helper.succeed();
    }
    public static void summonEntitySpawn(GameTestHelper helper) {
        helper.assertTrue(ModEntities.PYREWING.isBound(), "Pyrewing summon type missing");
        helper.assertTrue(ModEntities.FROSTMANE.isBound(), "Frostmane summon type missing");
        helper.assertTrue(ModEntities.TERRAGORN.isBound(), "Terragorn summon type missing");
        var creature = helper.spawn(ModEntities.PYREWING.get(), new BlockPos(3, 1, 3));
        helper.assertTrue(creature.isAlive(), "Pyrewing did not spawn in Minecraft");
        helper.succeed();
    }
    public static void lootBalance(GameTestHelper helper) {
        Random random = new Random(7319);
        int shards = 0, weapons = 0;
        for (int i = 0; i < 10000; i++) {
            CombatLoot.Drop drop = CombatLoot.roll(random, 0);
            if (drop == CombatLoot.Drop.AETHER_SHARD) shards++;
            else if (drop != CombatLoot.Drop.NONE) weapons++;
        }
        helper.assertTrue(shards > 700 && shards < 1200, "Aether shard drop chance is outside expected balance");
        helper.assertTrue(weapons > 80 && weapons < 180, "Rare weapon drop chance is outside expected balance");
        helper.succeed();
    }
}
