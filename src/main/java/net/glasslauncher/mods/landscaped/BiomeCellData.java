package net.glasslauncher.mods.landscaped;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Random;

public class BiomeCellData {
    public static final Cache<Long, BiomeCellData> CACHE = Caffeine.newBuilder().maximumSize(Short.MAX_VALUE).build();

    public static final int CELL_SIZE = 256;

    public final int cellEpicenterX;
    public final int cellEpicenterZ;
    public final Biome biome;

    private BiomeCellData(int cellEpicenterX, int cellEpicenterZ, Biome biome) {
        this.cellEpicenterX = cellEpicenterX;
        this.cellEpicenterZ = cellEpicenterZ;
        this.biome = biome;
    }

    // Disclaimer: I have zero clue how this works, but it seems to work so /shrug
    static long cellKey(int cellX, int cellZ, long worldSeed, int dimensionId) {
        long hash = worldSeed;

        hash ^= dimensionId * 0x165667B19E3779F9L;
        hash ^= (long) cellX * 0x9E3779B97F4A7C15L;
        hash ^= (long) cellZ * 0xC2B2AE3D27D4EB4FL;

        // final avalanche
        hash ^= (hash >>> 30);
        hash *= 0xBF58476D1CE4E5B9L;
        hash ^= (hash >>> 27);
        hash *= 0x94D049BB133111EBL;
        hash ^= (hash >>> 31);

        return hash;
    }

    public static BiomeCellData of(int cellX, int cellZ, World world) {
        long cellKey = cellKey(cellX, cellZ, world.getSeed(), world.dimension.id);
        return CACHE.get(cellKey, key -> {
            Random random = new Random(cellKey);
            return new BiomeCellData((cellX * CELL_SIZE) + random.nextInt(CELL_SIZE), (cellZ * CELL_SIZE) + random.nextInt(CELL_SIZE), BiomeDistributor.getCurrentDistributor().pickBiome(cellX, cellZ, world, random));
        });
    }
}