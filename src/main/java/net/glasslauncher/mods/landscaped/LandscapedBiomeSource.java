package net.glasslauncher.mods.landscaped;

import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.FixedBiomeSource;

import static net.glasslauncher.mods.landscaped.BiomeCellData.CELL_SIZE;

public interface LandscapedBiomeSource {
    double WARP_FREQ = 0.01;   // lower = larger features
    double WARP_AMP  = 32.0;   // max blocks to warp


    default Biome getBiomeAt(int worldX, int worldZ, World world) {
        if (this instanceof FixedBiomeSource fixedBiomeSource) {
            return fixedBiomeSource.getBiome(0, 0);
        }
        int cellX = Math.floorDiv(worldX, CELL_SIZE);
        int cellZ = Math.floorDiv(worldZ, CELL_SIZE);

        OctavePerlinNoiseSampler sampler = ((LandscapedBiomeSource) world.dimension.biomeSource).landscaped$getVoronoiSmootherSampler();

        // These are basically fake X and Y values to sample instead of the real one
        int warpedX = worldX + (int) (sampler.sample(worldX * WARP_FREQ, worldZ * WARP_FREQ) * WARP_AMP);
        int warpedZ = worldZ + (int) (sampler.sample((worldX + 1000) * WARP_FREQ, (worldZ - 1000) * WARP_FREQ) * WARP_AMP);

        double bestDist = Double.MAX_VALUE;
        Biome bestBiome = null;

        int neighborRadius = 1 + (int) Math.ceil(WARP_AMP / (float) CELL_SIZE);

        // Biome edge warp handling
        for (int cellOffsetX = -neighborRadius; cellOffsetX <= neighborRadius; cellOffsetX++) {
            for (int cellOffsetZ = -neighborRadius; cellOffsetZ <= neighborRadius; cellOffsetZ++) {
                BiomeCellData seed = BiomeCellData.of(cellX + cellOffsetX, cellZ + cellOffsetZ, world);

                double dxp = warpedX - seed.cellEpicenterX;
                double dzp = warpedZ - seed.cellEpicenterZ;
                double dist = dxp * dxp + dzp * dzp;

                if (dist < bestDist) {
                    bestDist = dist;
                    bestBiome = seed.biome;
                }
            }
        }

        return bestBiome;
    }

    default Biome[] getBiomesForArea(int startX, int startZ, int sizeX, int sizeZ, World world) {
        Biome[] biomes = new Biome[sizeX * sizeZ];

        world.method_1781().temperatureMap = new double[sizeX * sizeZ];
        world.method_1781().downfallMap = new double[sizeX * sizeZ];

        for (int posX = 0; posX < sizeX; posX++) {
            for (int posZ = 0; posZ < sizeZ; posZ++) {
                biomes[(posX * sizeZ) + posZ] = getBiomeAt(startX + posX, startZ + posZ, world);
            }
        }

        return biomes;
    }

    OctavePerlinNoiseSampler landscaped$getVoronoiSmootherSampler();
    World landscaped$getWorld();
    void landscaped$setWorld(World world);
}
