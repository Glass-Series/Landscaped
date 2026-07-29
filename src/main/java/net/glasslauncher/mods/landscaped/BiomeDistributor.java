package net.glasslauncher.mods.landscaped;

import lombok.Getter;
import net.glasslauncher.mods.landscaped.events.init.LandscapedConfig;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.Random;

public abstract class BiomeDistributor {
    protected final World world;

    public BiomeDistributor(World world) {
        this.world = world;
    }

    public static BiomeDistributor getCurrentDistributor() {
        return null;
    }

    public static void updateDistributor(World world) {
    }

    abstract public Biome pickBiome(int cellX, int cellZ, World world, Random random);


    public Biome[] getBiomesForValues(double temperature, double downfall) {
        Biome[] closestBiomes = new Biome[0];
        double closestDist = Double.MAX_VALUE;

        for (Biome biome : LandscapedBiomeRegistry.INSTANCE.stream().toList()) {
            if (biome.landscaped$getTemperature() == -1 || ((LandscapedBiome) biome).landscaped$getDownfall() == -1) {
                continue;
            }
            double dist = Math.sqrt(((((LandscapedBiome) biome).landscaped$getTemperature() - temperature) * (((LandscapedBiome) biome).landscaped$getTemperature() - temperature)) + (((((LandscapedBiome) biome).landscaped$getDownfall() - downfall) * (((LandscapedBiome) biome).landscaped$getDownfall() - downfall)) / 2d));
            if (dist == closestDist) {
                closestBiomes = Arrays.copyOf(closestBiomes, closestBiomes.length + 1);
                closestBiomes[closestBiomes.length - 1] = biome;
            }
            else if (dist < closestDist) {
                closestBiomes = new Biome[] {biome};
                closestDist = dist;
            }
        }
        return closestBiomes;
    }
}
