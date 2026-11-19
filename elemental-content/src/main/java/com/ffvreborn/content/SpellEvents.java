package com.ffvreborn.content;

import com.ffvreborn.abilities.AbilityCatalog;
import com.ffvreborn.abilities.AbilityDefinition;
import com.ffvreborn.core.PlayerProgression;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FinalFantasyVReborn.MOD_ID)
public final class SpellEvents {
    private static final Map<UUID, Map<String, Long>> COOLDOWNS = new ConcurrentHashMap<>();
    private SpellEvents() {}
    @SubscribeEvent
    public static void castSigil(PlayerInteractEvent.RightClickItem event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        String id = abilityFor(event.getItemStack().getItem());
        if (id == null) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        AbilityDefinition ability = AbilityCatalog.get(id);
        PlayerProgression data = player.getData(RpgAttachments.PLAYER);
        if (!data.hasAbility(id)) {
            try {
                data = data.learnAbility(id, ability.requiredLevel(), ability.abpCost());
                player.setData(RpgAttachments.PLAYER, data);
                player.syncData(RpgAttachments.PLAYER);
                player.sendSystemMessage(Component.literal("Técnica aprendida: " + id + ". Use novamente para conjurar."));
            } catch (IllegalStateException error) {
                player.sendSystemMessage(Component.literal("Não foi possível aprender: " + error.getMessage()));
            }
            return;
        }
        if (!data.activeJob().equals(ability.jobId())) {
            player.sendSystemMessage(Component.literal("Esta técnica exige a profissão " + ability.jobId() + "."));
            return;
        }
        long now = player.level().getGameTime();
        long readyAt = COOLDOWNS.getOrDefault(player.getUUID(), Map.of()).getOrDefault(id, 0L);
        if (now < readyAt) {
            player.sendSystemMessage(Component.literal("Técnica em recarga: " + (readyAt - now) + " ticks."));
            return;
        }
        try {
            data = data.spendMana(ability.manaCost());
        } catch (IllegalStateException error) {
            player.sendSystemMessage(Component.literal("Mana insuficiente."));
            return;
        }
        player.setData(RpgAttachments.PLAYER, data);
        player.syncData(RpgAttachments.PLAYER);
        execute(player, ability.effect());
        COOLDOWNS.computeIfAbsent(player.getUUID(), ignored -> new ConcurrentHashMap<>()).put(id, now + ability.cooldownTicks());
        player.sendSystemMessage(Component.literal(id.toUpperCase() + " conjurado • " + ability.manaCost() + " PM"));
    }
    private static String abilityFor(Item item) {
        if (item == ModItems.FIRE_SIGIL.get()) return "fire";
        if (item == ModItems.ICE_SIGIL.get()) return "ice";
        if (item == ModItems.THUNDER_SIGIL.get()) return "thunder";
        if (item == ModItems.FLARE_SIGIL.get()) return "flare";
        if (item == ModItems.CURE_SIGIL.get()) return "cure";
        if (item == ModItems.REGEN_SIGIL.get()) return "regen";
        if (item == ModItems.HASTE_SIGIL.get()) return "haste";
        if (item == ModItems.SLOW_SIGIL.get()) return "slow";
        if (item == ModItems.BACKSTAB_SIGIL.get()) return "backstab";
        if (item == ModItems.CHAKRA_SIGIL.get()) return "chakra";
        if (item == ModItems.JUMP_SIGIL.get()) return "jump";
        if (item == ModItems.SHURIKEN_SIGIL.get()) return "shuriken";
        return null;
    }
    private static void execute(ServerPlayer player, String effect) {
        ServerLevel level = player.level() instanceof ServerLevel server ? server : null;
        if (level == null) return;
        Monster target = level.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(10),
            mob -> mob.isAlive() && mob.distanceToSqr(player) <= 100)
            .stream().min(Comparator.comparingDouble(player::distanceToSqr)).orElse(null);
        switch (effect) {
            case "fire_burst" -> damageTarget(player, target, level, 8.0f, ModParticles.AETHER_FLARE.get(), true);
            case "frost_lance" -> {
                damageTarget(player, target, level, 7.0f, ModParticles.FROST_SHARD.get(), false);
                if (target != null) target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 1));
            }
            case "storm_arc" -> damageTarget(player, target, level, 11.0f, ModParticles.RUNE_SPARK.get(), false);
            case "aether_flare" -> damageTarget(player, target, level, 22.0f, ModParticles.AETHER_FLARE.get(), true);
            case "mending_light" -> { player.heal(8.0f); burst(level, player, ModParticles.RUNE_SPARK.get(), 24); }
            case "verdant_regrowth" -> { player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 240, 1)); burst(level, player, ModParticles.RUNE_SPARK.get(), 30); }
            case "quickening" -> { player.addEffect(new MobEffectInstance(MobEffects.SPEED, 240, 1)); burst(level, player, ModParticles.RUNE_SPARK.get(), 20); }
            case "temporal_snare" -> {
                if (target != null) target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 160, 3));
                burst(level, player, ModParticles.RUNE_SPARK.get(), 22);
            }
            case "shadow_strike" -> damageTarget(player, target, level, 14.0f, ModParticles.RUNE_SPARK.get(), false);
            case "inner_focus" -> { player.heal(5.0f); burst(level, player, ModParticles.RUNE_SPARK.get(), 18); }
            case "skyfall" -> {
                player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 100, 2));
                if (target != null) target.hurt(player.damageSources().playerAttack(player), 10.0f);
                burst(level, player, ModParticles.FROST_SHARD.get(), 26);
            }
            case "wind_cut" -> damageTarget(player, target, level, 9.0f, ModParticles.RUNE_SPARK.get(), false);
            default -> { }
        }
    }
    private static void damageTarget(ServerPlayer player, Monster target, ServerLevel level, float damage,
        net.minecraft.core.particles.ParticleOptions particle, boolean ignite) {
        if (target != null) {
            target.hurt(player.damageSources().magic(), damage);
            if (ignite) target.igniteForSeconds(4);
            level.sendParticles(particle, target.getX(), target.getY() + target.getBbHeight() * 0.55,
                target.getZ(), 28, 0.45, 0.55, 0.45, 0.08);
        } else burst(level, player, particle, 14);
    }
    private static void burst(ServerLevel level, ServerPlayer player,
        net.minecraft.core.particles.ParticleOptions particle, int count) {
        level.sendParticles(particle, player.getX(), player.getY() + 1.0, player.getZ(),
            count, 0.45, 0.65, 0.45, 0.08);
    }
}
