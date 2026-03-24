package net.glasslauncher.mods.landscaped;

import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;

import java.util.Random;

public class ScaledOctaveSimplexSampler extends OctaveSimplexNoiseSampler {
    private final double scale;

    public ScaledOctaveSimplexSampler(Random random, int octaves, double scale) {
        super(random, octaves);
        this.scale = scale;
    }

    @Override
    public double[] sample(double[] map, double x, double y, int width, int height, double d, double e, double f, double g) {
        return super.sample(map, x, y, width, height, d / scale, e / scale, f, g);
    }
}
