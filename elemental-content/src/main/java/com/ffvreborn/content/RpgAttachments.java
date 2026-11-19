package com.ffvreborn.content;
import com.ffvreborn.core.PlayerProgression;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
public final class RpgAttachments {
    private static final var CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.STRING.optionalFieldOf("active_job", "freelancer").forGetter(PlayerProgression::activeJob),
        Codec.unboundedMap(Codec.STRING, Codec.LONG).optionalFieldOf("xp", java.util.Map.of()).forGetter(PlayerProgression::xpByJob),
        Codec.unboundedMap(Codec.STRING, Codec.INT).optionalFieldOf("levels", java.util.Map.of("freelancer", 1)).forGetter(PlayerProgression::levelsByJob),
        Codec.unboundedMap(Codec.STRING, Codec.INT).optionalFieldOf("abp", java.util.Map.of()).forGetter(PlayerProgression::abpByJob),
        Codec.INT.optionalFieldOf("mana", 100).forGetter(PlayerProgression::mana)
    ).apply(instance, PlayerProgression::new));
    public static final DeferredRegister<AttachmentType<?>> TYPES =
        DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, FinalFantasyVReborn.MOD_ID);
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerProgression>> PLAYER =
        TYPES.register("player_progression", () -> AttachmentType.builder(PlayerProgression::fresh)
            .serialize(CODEC).copyOnDeath().build());
    private RpgAttachments() {}
}
