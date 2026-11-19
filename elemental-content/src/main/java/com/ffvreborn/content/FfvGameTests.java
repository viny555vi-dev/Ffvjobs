package com.ffvreborn.content;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
        event.registerTest(
            Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, "item_registered"),
            new FunctionGameTestInstance(FinalFantasyVReborn.ITEM_TEST.getKey(), data)
        );
        event.registerTest(
            Identifier.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, "job_progress"),
            new FunctionGameTestInstance(FinalFantasyVReborn.JOB_TEST.getKey(), data)
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
}
