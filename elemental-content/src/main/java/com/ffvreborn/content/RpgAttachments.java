package com.ffvreborn.content;

import com.ffvreborn.core.PlayerProgression;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class RpgAttachments {
    public static final DeferredRegister<AttachmentType<?>> TYPES =
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, FinalFantasyVReborn.MOD_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerProgression>> PLAYER =
        TYPES.register("player_progression", () -> AttachmentType.builder(PlayerProgression::fresh)
            .serialize(PlayerProgression.CODEC).copyOnDeath().build());
    private RpgAttachments() {}
}
