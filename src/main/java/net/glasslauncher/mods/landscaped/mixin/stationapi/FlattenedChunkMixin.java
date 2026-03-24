package net.glasslauncher.mods.landscaped.mixin.stationapi;

import net.glasslauncher.mods.landscaped.LandscapedChunk;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FlattenedChunk.class)
public class FlattenedChunkMixin implements LandscapedChunk {
    @Unique
    Biome[] biomes;

    @Override
    public Biome[] landscaped$getBiomes() {
        return biomes;
    }

    @Override
    public void landscaped$setBiomes(Biome[] biomes) {
        this.biomes = biomes;
    }
}
