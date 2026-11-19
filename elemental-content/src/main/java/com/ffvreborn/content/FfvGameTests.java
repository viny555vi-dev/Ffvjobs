package com.ffvreborn.content;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.ffvreborn.core.JobDefinition;
import com.ffvreborn.core.JobService;
import com.ffvreborn.core.PlayerRpgData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.GameTestHolder;
@GameTestHolder(FinalFantasyVReborn.MOD_ID)
public final class FfvGameTests {
    @GameTest(template = "empty")
    public static void modItemIsRegistered(GameTestHelper helper) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(FinalFantasyVReborn.MOD_ID, "crystal_of_dawn");
        helper.assertTrue(BuiltInRegistries.ITEM.get(id) != Items.AIR, "Crystal of Dawn item was not registered");
        helper.succeed();
    }
    @GameTest(template = "empty")
    public static void jobProgressWorksInsideMinecraftServer(GameTestHelper helper) {
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
