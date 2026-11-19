package com.ffvreborn.content;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.LootTableLoadEvent;

/** Adds the Reborn relic pool to vanilla dungeon chests without replacing their original loot. */
@EventBusSubscriber(modid = FinalFantasyVReborn.MOD_ID)
public final class DungeonLootEvents {
    private static final Identifier SIMPLE_DUNGEON = Identifier.withDefaultNamespace("chests/simple_dungeon");
    private DungeonLootEvents() {}
    @SubscribeEvent
    public static void addDungeonRelics(LootTableLoadEvent event) {
        if (!SIMPLE_DUNGEON.equals(event.getName())) return;
        LootPool relics = LootPool.lootPool().name("ffv_reborn_dungeon_relics")
            .setRolls(ContextIntProviders.exactly(1))
            .add(LootItem.lootTableItem(ModItems.AETHER_SHARD.get()).setWeight(7))
            .add(LootItem.lootTableItem(ModItems.EMBERSTEEL_BLADE.get()).setWeight(2))
            .add(LootItem.lootTableItem(ModItems.TIDEGLASS_STAFF.get()).setWeight(2))
            .add(LootItem.lootTableItem(ModItems.WYRMFANG_SPEAR.get()).setWeight(2))
            .add(LootItem.lootTableItem(ModItems.VERDANT_LONGBOW.get()).setWeight(2))
            .build();
        event.getTable().addPool(relics);
    }
}
