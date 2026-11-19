package com.ffvreborn.content;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.ffvreborn.abilities.AbilityDefinition;
import com.ffvreborn.abilities.AbilityService;
import com.ffvreborn.core.JobDefinition;
import com.ffvreborn.core.JobService;
import com.ffvreborn.core.PlayerRpgData;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
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
    }
    private static void register(RegisterGameTestsEvent event, String id,
        net.neoforged.neoforge.registries.DeferredHolder<java.util.function.Consumer<GameTestHelper>, java.util.function.Consumer<GameTestHelper>> function,
        TestData<Holder<TestEnvironmentDefinition<?>>> data) {
        event.registerTest(
            Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, id),
            new FunctionGameTestInstance(function.getKey(), data)
        );
    }
    public static void itemRegistered(GameTestHelper helper) {
        helper.assertTrue(FinalFantasyVReborn.CRYSTAL_OF_DAWN.isBound(), "Crystal of Dawn registry object was not bound");
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
        SummonService summons = new SummonService(List.of(
            new SummonService.Summon("ifrit", 25, 100, 1, "fire_burst")
        ));
        String effect = summons.invoke(player, "ifrit", 10);
        helper.assertTrue("fire_burst".equals(effect), "Ifrit returned the wrong effect");
        helper.assertTrue(player.mana() == 75, "Ifrit did not consume 25 mana");
        helper.succeed();
    }
}
