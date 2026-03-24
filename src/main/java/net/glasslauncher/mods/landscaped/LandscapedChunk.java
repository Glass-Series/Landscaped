package net.glasslauncher.mods.landscaped;

import net.minecraft.world.biome.Biome;

public interface LandscapedChunk {
    Biome[] landscaped$getBiomes();
    void landscaped$setBiomes(Biome[] biomes);
}
