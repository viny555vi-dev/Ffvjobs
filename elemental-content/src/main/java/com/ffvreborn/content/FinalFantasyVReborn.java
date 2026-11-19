package com.ffvreborn.content;
import java.util.function.Consumer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
@Mod(FinalFantasyVReborn.MOD_ID)
public final class FinalFantasyVReborn {
    public static final String MOD_ID = "ffv_reborn";
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredHolder<Item, Item> CRYSTAL_OF_DAWN = ITEMS.registerSimpleItem("crystal_of_dawn");
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
    public FinalFantasyVReborn(IEventBus modBus) {
        ITEMS.register(modBus);
        TEST_FUNCTIONS.register(modBus);
        modBus.addListener(FfvGameTests::registerTests);
    }
}
