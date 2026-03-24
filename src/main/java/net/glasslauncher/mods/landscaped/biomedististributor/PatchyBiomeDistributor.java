package net.glasslauncher.mods.landscaped.biomedististributor;

import net.glasslauncher.mods.landscaped.BiomeDistributor;
import net.glasslauncher.mods.landscaped.LandscapedMath;
import net.glasslauncher.mods.landscaped.events.init.LandscapedBiomesInit;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.Random;

import static net.glasslauncher.mods.landscaped.BiomeCellData.CELL_SIZE;

/**
 * This is essentially 1:1 with how modern handles biome placement.
 */
public class PatchyBiomeDistributor extends BiomeDistributor {
    public PatchyBiomeDistributor(World mainWorld) {
        super(mainWorld);
    }

    @Override
    public Biome pickBiome(int cellX, int cellZ, World world, Random random) {
        double temperature = world.dimension.biomeSource.temperatureSampler.sample(null, cellX * CELL_SIZE, cellZ * CELL_SIZE, 1, 1, 0.025F, 0.025F, 0.5F)[0];
        double downfall = world.dimension.biomeSource.downfallSampler.sample(null, cellX * CELL_SIZE, cellZ * CELL_SIZE, 1, 1, 0.05F, 0.05F, 0.3333333333333333)[0];

        temperature /= 8;
        downfall /= 8;
        temperature = LandscapedMath.clamp(temperature + 0.55, 0, 1);
        downfall = LandscapedMath.clamp(downfall + 0.55, 0, 1);

        Biome[] closestBiomes = getBiomesForValues(temperature, downfall);

        if (closestBiomes.length == 1) {
            return closestBiomes[0];
        }

        return closestBiomes[random.nextInt(closestBiomes.length)];
    }
}
