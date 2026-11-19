package com.ffvreborn.content;

import com.ffvreborn.core.PlayerProgression;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FinalFantasyVReborn.MOD_ID)
public final class SummonEvents {
    private static final Map<UUID, Long> READY_AT = new ConcurrentHashMap<>();
    private SummonEvents() {}
    @SubscribeEvent
    public static void summon(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        EntityType<? extends Monster> type = summonFor(event.getItemStack().getItem());
        if (type == null) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        PlayerProgression data = player.getData(RpgAttachments.PLAYER);
        if (!"summoner".equals(data.activeJob())) {
            player.displayClientMessage(Component.literal("A profissão Invocador é necessária."), true);
            return;
        }
        long now = player.level().getGameTime();
        if (now < READY_AT.getOrDefault(player.getUUID(), 0L)) {
            player.displayClientMessage(Component.literal("O vínculo de invocação ainda está se recuperando."), true);
            return;
        }
        int cost = type == ModEntities.PYREWING.get() ? 35 : type == ModEntities.FROSTMANE.get() ? 40 : 45;
        if (data.mana() < cost) {
            player.displayClientMessage(Component.literal("Mana insuficiente para esta invocação."), true);
            return;
        }
        player.setData(RpgAttachments.PLAYER, data.spendMana(cost));
        player.syncData(RpgAttachments.PLAYER);
        FFVSummon creature = new FFVSummon(type, player.level());
        creature.bindSummoner(player.getUUID());
        creature.moveTo(player.getX() + 1.2, player.getY(), player.getZ() + 0.8, player.getYRot(), 0);
        creature.setTarget(player.level().getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(14),
            mob -> mob.isAlive() && !(mob instanceof FFVSummon)).stream().findFirst().orElse(null));
        player.level().addFreshEntity(creature);
        if (player.level() instanceof net.minecraft.server.level.ServerLevel level) {
            level.sendParticles(ModParticles.AETHER_FLARE.get(), creature.getX(), creature.getY() + 0.7, creature.getZ(), 35, 0.7, 0.8, 0.7, 0.08);
            level.sendParticles(ModParticles.RUNE_SPARK.get(), creature.getX(), creature.getY() + 0.5, creature.getZ(), 45, 0.6, 0.7, 0.6, 0.15);
        }
        READY_AT.put(player.getUUID(), now + 240);
        player.displayClientMessage(Component.literal("Invocação manifestada. Duração: 90 segundos."), true);
    }
    private static EntityType<? extends Monster> summonFor(Item item) {
        if (item == ModItems.PYREWING_SIGIL.get()) return ModEntities.PYREWING.get();
        if (item == ModItems.FROSTMANE_SIGIL.get()) return ModEntities.FROSTMANE.get();
        if (item == ModItems.TERRAGORN_SIGIL.get()) return ModEntities.TERRAGORN.get();
        return null;
    }
}
