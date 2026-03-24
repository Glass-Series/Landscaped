package net.glasslauncher.mods.landscaped;

import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;

import java.util.Random;

public class ScaledOctavePerlinSampler extends OctavePerlinNoiseSampler {
    final double scale;
    public ScaledOctavePerlinSampler(Random random, int i, double scale) {
        super(random, i);
        this.scale = scale;
    }

    @Override
    public double[] create(double[] map, double x, double y, double z, int width, int height, int depth, double d, double e, double f) {
        return super.create(map, x, y, z, width, height, depth, d / scale, e / scale, f / scale);
    }
}
