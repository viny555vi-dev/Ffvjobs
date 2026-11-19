package com.ffvreborn.content;

import com.ffvreborn.core.PlayerProgression;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/** Crystal of Dawn cycles the world-local profession without commands or external UI dependencies. */
@EventBusSubscriber(modid = FinalFantasyVReborn.MOD_ID)
public final class JobSwitchEvents {
    private static final List<String> JOBS = List.of(
        "freelancer","knight","monk","thief","black_mage","white_mage","blue_mage","red_mage",
        "time_mage","summoner","berserker","ninja","samurai","dragoon","bard","ranger","geomancer","mimic"
    );
    private JobSwitchEvents() {}
    @SubscribeEvent
    public static void cycleJob(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || event.getItemStack().getItem() != ModItems.CRYSTAL_OF_DAWN.get()) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        PlayerProgression data = player.getData(RpgAttachments.PLAYER);
        int index = JOBS.indexOf(data.activeJob());
        String next = JOBS.get((index + 1 + JOBS.size()) % JOBS.size());
        player.setData(RpgAttachments.PLAYER, data.withJob(next));
        player.syncData(RpgAttachments.PLAYER);
        player.displayClientMessage(Component.literal("Profissão ativa: " + next.toUpperCase() +
            " • Nv. " + data.level(next) + " • ABP " + data.abp(next)), true);
    }
}
