package com.ffvreborn.content;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
@Mod(FinalFantasyVReborn.MOD_ID)
public final class FinalFantasyVReborn {
    public static final String MOD_ID = "ffv_reborn";
    public FinalFantasyVReborn(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        RpgAttachments.TYPES.register(modBus);
        ModEntities.register(modBus);
    }
}
