package net.glasslauncher.mods.landscaped.biomedististributor;

import net.glasslauncher.mods.landscaped.BiomeDistributor;
import net.glasslauncher.mods.landscaped.LandscapedBiome;
import net.glasslauncher.mods.landscaped.LandscapedBiomeRegistry;
import net.glasslauncher.mods.landscaped.LandscapedMath;
import net.glasslauncher.mods.landscaped.events.init.LandscapedBiomesInit;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Arrays;
import java.util.Random;

import static net.glasslauncher.mods.landscaped.BiomeCellData.CELL_SIZE;

/**
 * This is a system that's pretty similar to how vintage story handles biome distribution.
 * Great if you want predictable/realistic biome distribution.
 */
public class EquatorialBiomeDistributor extends BiomeDistributor {
    private static final double EQUATOR_DISTANCE = 16000;
    private static final double EQUATOR_HALF = EQUATOR_DISTANCE / 2;
    private static final double TEMPERATE_BIAS = 1.1; // 1.0 is standard distribution.
    private static final double EDGE_BIAS = 1.5; // Edge here being cold/hot temps.

    public EquatorialBiomeDistributor(World mainWorld) {
        super(mainWorld);
    }

    @Override
    public Biome pickBiome(int cellX, int cellZ, World world, Random random) {
        int worldPos = cellZ * CELL_SIZE;
        double temperature = Math.abs((worldPos) / EQUATOR_HALF);
        temperature = 0.5 * (1 + Math.sin(2 * Math.PI * temperature - Math.PI / 2));
        temperature = 0.5 + Math.signum(temperature - 0.5) * Math.pow(Math.abs(temperature - 0.5) * 2, TEMPERATE_BIAS) * 0.5;

        double d = Math.abs(temperature - 0.5) * 2.0;
        d = Math.pow(d, 1.0 + EDGE_BIAS);
        temperature = 0.5 + Math.signum(temperature - 0.5) * d * 0.5;

        Biome[] closestBiomes = getBiomesForValues(temperature, 0);

        if (closestBiomes.length == 1) {
            return closestBiomes[0];
        }

        return closestBiomes[random.nextInt(closestBiomes.length)];
    }

    @Override
    public Biome[] getBiomesForValues(double temperature, double downfall) {
        Biome[] closestBiomes = new Biome[0];
        double closestDist = Double.MAX_VALUE;

        for (Biome biome : LandscapedBiomeRegistry.INSTANCE.stream().toList()) {
            if (((LandscapedBiome) biome).landscaped$getTemperature() == -1 || ((LandscapedBiome) biome).landscaped$getDownfall() == -1) {
                continue;
            }
            int dist = (int) Math.round(Math.abs(((LandscapedBiome) biome).landscaped$getTemperature() - temperature) * 10d);
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
