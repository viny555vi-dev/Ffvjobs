package com.ffvreborn.content;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.BowItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FinalFantasyVReborn.MOD_ID);
    public static final DeferredHolder<Item, Item> AETHER_SHARD = ITEMS.registerSimpleItem("aether_shard");
    public static final DeferredHolder<Item, Item> EMBERSTEEL_BLADE = ITEMS.registerSimpleItem("embersteel_blade");
    public static final DeferredHolder<Item, Item> TIDEGLASS_STAFF = ITEMS.registerSimpleItem("tideglass_staff");
    public static final DeferredHolder<Item, Item> WYRMFANG_SPEAR = ITEMS.registerSimpleItem("wyrmfang_spear");
    public static final DeferredHolder<Item, Item> VERDANT_LONGBOW = ITEMS.registerItem("verdant_longbow", BowItem::new, properties -> properties.durability(480));
    public static final DeferredHolder<Item, Item> CRYSTAL_OF_DAWN = ITEMS.registerSimpleItem("crystal_of_dawn");
    public static final DeferredHolder<Item, Item> FIRE_SIGIL = ITEMS.registerSimpleItem("fire_sigil");
    public static final DeferredHolder<Item, Item> ICE_SIGIL = ITEMS.registerSimpleItem("ice_sigil");
    public static final DeferredHolder<Item, Item> THUNDER_SIGIL = ITEMS.registerSimpleItem("thunder_sigil");
    public static final DeferredHolder<Item, Item> FLARE_SIGIL = ITEMS.registerSimpleItem("flare_sigil");
    public static final DeferredHolder<Item, Item> CURE_SIGIL = ITEMS.registerSimpleItem("cure_sigil");
    public static final DeferredHolder<Item, Item> REGEN_SIGIL = ITEMS.registerSimpleItem("regen_sigil");
    public static final DeferredHolder<Item, Item> HASTE_SIGIL = ITEMS.registerSimpleItem("haste_sigil");
    public static final DeferredHolder<Item, Item> SLOW_SIGIL = ITEMS.registerSimpleItem("slow_sigil");
    public static final DeferredHolder<Item, Item> BACKSTAB_SIGIL = ITEMS.registerSimpleItem("backstab_sigil");
    public static final DeferredHolder<Item, Item> CHAKRA_SIGIL = ITEMS.registerSimpleItem("chakra_sigil");
    public static final DeferredHolder<Item, Item> JUMP_SIGIL = ITEMS.registerSimpleItem("jump_sigil");
    public static final DeferredHolder<Item, Item> SHURIKEN_SIGIL = ITEMS.registerSimpleItem("shuriken_sigil");
    public static final DeferredHolder<Item, Item> PYREWING_SIGIL = ITEMS.registerSimpleItem("pyrewing_sigil");
    public static final DeferredHolder<Item, Item> FROSTMANE_SIGIL = ITEMS.registerSimpleItem("frostmane_sigil");
    public static final DeferredHolder<Item, Item> TERRAGORN_SIGIL = ITEMS.registerSimpleItem("terragorn_sigil");
    private ModItems() {}
}
