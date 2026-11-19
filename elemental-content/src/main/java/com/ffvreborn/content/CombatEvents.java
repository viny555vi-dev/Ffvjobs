package com.ffvreborn.content;

import com.ffvreborn.core.PlayerProgression;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

@EventBusSubscriber(modid = FinalFantasyVReborn.MOD_ID)
public final class CombatEvents {
    private CombatEvents() {}


    @SubscribeEvent
    public static void applyWeaponMastery(LivingIncomingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;
        var weapon = player.getMainHandItem().getItem();
        float bonus = weapon == ModItems.EMBERSTEEL_BLADE.get() ? 4.0f
            : weapon == ModItems.TIDEGLASS_STAFF.get() ? 3.0f
            : weapon == ModItems.WYRMFANG_SPEAR.get() ? 5.0f : 0.0f;
        if (bonus > 0.0f) event.setAmount(event.getAmount() + bonus);
    }

    @SubscribeEvent
    public static void onHostileKilled(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof ServerPlayer player) || !(victim instanceof Monster) || victim instanceof FFVSummon) return;
        PlayerProgression current = player.getData(RpgAttachments.PLAYER);
        int xp = CombatLoot.xpForKill(victim.getMaxHealth(), true);
        PlayerProgression updated = current.awardKillXp(xp);
        player.setData(RpgAttachments.PLAYER, updated);
        player.syncData(RpgAttachments.PLAYER);
        player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
            current.activeJob().toUpperCase() + "  +" + xp + " EXP  •  Nv. " + updated.level(current.activeJob()) +
            "  •  ABP " + updated.abp(current.activeJob())));
    }

    @SubscribeEvent
    public static void onHostileDrops(LivingDropsEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof ServerPlayer player) || !(victim instanceof Monster) || victim instanceof FFVSummon) return;
        float luck = (float) player.getLuck();
        CombatLoot.Drop drop = CombatLoot.roll(new java.util.Random(victim.getRandom().nextLong()), luck);
        if (drop == CombatLoot.Drop.NONE) return;
        var item = switch (drop) {
            case AETHER_SHARD -> ModItems.AETHER_SHARD.get();
            case EMBERSTEEL_BLADE -> ModItems.EMBERSTEEL_BLADE.get();
            case TIDEGLASS_STAFF -> ModItems.TIDEGLASS_STAFF.get();
            case WYRMFANG_SPEAR -> ModItems.WYRMFANG_SPEAR.get();
            case VERDANT_LONGBOW -> ModItems.VERDANT_LONGBOW.get();
            default -> null;
        };
        if (item != null) event.getDrops().add(new ItemEntity(victim.level(), victim.getX(), victim.getY() + 0.25, victim.getZ(), new ItemStack(item)));
    }
}
