package net.glasslauncher.mods.landscaped.mixin;

import net.glasslauncher.mods.landscaped.LandscapedBiome;
import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(Biome.class)
public class CustomBiomesMixin implements LandscapedBiome {
    @Unique
    List<Block> grasses = new ArrayList<>();
    @Unique
    List<Block> flowers = new ArrayList<>();
    @Unique
    float temperature = -1;
    @Unique
    float downfall = -1;
    @Unique
    Identifier identifier;

    @Override
    public List<Block> landscaped$getGrasses() {
        return grasses;
    }

    @Unique
    public Biome landscaped$setGrasses(List<Block> grasses) {
        this.grasses = grasses;
        return (Biome) (Object) this;
    }

    @Unique
    public List<Block> landscaped$getFlowers() {
        return flowers;
    }

    @Unique
    public Biome landscaped$setFlowers(List<Block> flowers) {
        this.flowers = flowers;
        return (Biome) (Object) this;
    }

    @Override
    public float landscaped$getTemperature() {
        return temperature;
    }

    @Override
    public float landscaped$getDownfall() {
        return downfall;
    }

    @Override
    public Biome landscaped$setTemperature(float temperature) {
        this.temperature = temperature;
        return (Biome) (Object) this;
    }

    @Override
    public Biome landscaped$setDownfall(float downfall) {
        this.downfall = downfall;
        return (Biome) (Object) this;
    }

    @Override
    public Identifier landscaped$getIdentifier() {
        return identifier;
    }

    @Override
    public Biome landscaped$setIdentifier(Identifier identifier) {
        this.identifier = identifier;
        return (Biome) (Object) this;
    }
}
