package com.ffvreborn.content;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
/** Internal luminous materials used as the source textures for the custom particle renderer. */
public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(FinalFantasyVReborn.MOD_ID);
    public static final DeferredHolder<Block, Block> AETHER_PRISM = BLOCKS.registerSimpleBlock("aether_prism",
        () -> BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.AMETHYST_CLUSTER).lightLevel(state -> 9).noLootTable());
    public static final DeferredHolder<Block, Block> FROST_PRISM = BLOCKS.registerSimpleBlock("frost_prism",
        () -> BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.GLASS).lightLevel(state -> 5).noLootTable());
    public static final DeferredHolder<Block, Block> RUNE_PRISM = BLOCKS.registerSimpleBlock("rune_prism",
        () -> BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.AMETHYST_CLUSTER).lightLevel(state -> 7).noLootTable());
    private ModBlocks() {}
}
