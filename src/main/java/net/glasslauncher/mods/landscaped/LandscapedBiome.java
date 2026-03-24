package net.glasslauncher.mods.landscaped;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

public interface LandscapedBiome {
    List<Block> landscaped$getGrasses();
    Biome landscaped$setGrasses(List<Block> blocks);
    List<Block> landscaped$getFlowers();
    Biome landscaped$setFlowers(List<Block> blocks);

    /**
     * @return range from 0.0f to 1.0f
     */
    float landscaped$getTemperature();

    /**
     * @return range from 0.0f to 1.0f
     */
    float landscaped$getDownfall();

    /**
     * range from 0.0f to 1.0f
     */
    LandscapedBiome landscaped$setTemperature(float temperature);

    /**
     * range from 0.0f to 1.0f
     */
    LandscapedBiome landscaped$setDownfall(float downfall);

    Identifier landscaped$getIdentifier();
    LandscapedBiome landscaped$setIdentifier(Identifier identifier);
}
