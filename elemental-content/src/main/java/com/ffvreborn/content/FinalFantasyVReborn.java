package com.ffvreborn.content;
import java.util.function.Consumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
@Mod(FinalFantasyVReborn.MOD_ID)
public final class FinalFantasyVReborn {
    public static final String MOD_ID = "ffv_reborn";
    public static final DeferredRegister<Consumer<GameTestHelper>> TEST_FUNCTIONS =
        DeferredRegister.create(BuiltInRegistries.TEST_FUNCTION, MOD_ID);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ITEM_TEST =
        TEST_FUNCTIONS.register("item_registered", () -> FfvGameTests::itemRegistered);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> JOB_TEST =
        TEST_FUNCTIONS.register("job_progress", () -> FfvGameTests::jobProgressWorks);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ABILITY_TEST =
        TEST_FUNCTIONS.register("ability_cast", () -> FfvGameTests::abilityCanBeLearnedAndCast);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> SUMMON_TEST =
        TEST_FUNCTIONS.register("summon_cast", () -> FfvGameTests::summonCanBeInvoked);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> PROGRESSION_TEST =
        TEST_FUNCTIONS.register("combat_progression", () -> FfvGameTests::combatProgression);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> BESTIARY_TEST =
        TEST_FUNCTIONS.register("bestiary_spawn", () -> FfvGameTests::bestiarySpawn);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> LOOT_TEST =
        TEST_FUNCTIONS.register("loot_balance", () -> FfvGameTests::lootBalance);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> ABILITY_PROGRESS_TEST =
        TEST_FUNCTIONS.register("ability_progression", () -> FfvGameTests::abilityProgression);
    public static final DeferredHolder<Consumer<GameTestHelper>, Consumer<GameTestHelper>> SUMMON_ENTITY_TEST =
        TEST_FUNCTIONS.register("summon_entity", () -> FfvGameTests::summonEntitySpawn);
    public FinalFantasyVReborn(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        ModBlocks.BLOCKS.register(modBus);
        RpgAttachments.TYPES.register(modBus);
        ModEntities.register(modBus);
        ModParticles.TYPES.register(modBus);
        TEST_FUNCTIONS.register(modBus);
        modBus.addListener(FfvGameTests::registerTests);
    }
}
