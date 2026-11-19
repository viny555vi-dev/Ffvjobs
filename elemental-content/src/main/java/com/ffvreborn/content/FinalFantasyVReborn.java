package com.ffvreborn.content;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
@Mod(FinalFantasyVReborn.MOD_ID)
public final class FinalFantasyVReborn {
    public static final String MOD_ID = "ffv_reborn";
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredItem<Item> CRYSTAL_OF_DAWN = ITEMS.registerSimpleItem("crystal_of_dawn");
    public FinalFantasyVReborn(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}
