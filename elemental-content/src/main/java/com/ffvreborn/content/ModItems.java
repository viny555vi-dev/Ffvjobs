package com.ffvreborn.content;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FinalFantasyVReborn.MOD_ID);
    public static final DeferredHolder<Item, Item> AETHER_SHARD = ITEMS.registerSimpleItem("aether_shard");
    public static final DeferredHolder<Item, Item> EMBERSTEEL_BLADE = ITEMS.registerSimpleItem("embersteel_blade");
    public static final DeferredHolder<Item, Item> TIDEGLASS_STAFF = ITEMS.registerSimpleItem("tideglass_staff");
    public static final DeferredHolder<Item, Item> WYRMFANG_SPEAR = ITEMS.registerSimpleItem("wyrmfang_spear");
    public static final DeferredHolder<Item, Item> VERDANT_LONGBOW = ITEMS.registerSimpleItem("verdant_longbow");
    public static final DeferredHolder<Item, Item> CRYSTAL_OF_DAWN = ITEMS.registerSimpleItem("crystal_of_dawn");
    private ModItems() {}
}
