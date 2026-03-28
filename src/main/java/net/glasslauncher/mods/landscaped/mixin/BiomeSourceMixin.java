package net.glasslauncher.mods.landscaped.mixin;

import com.github.benmanes.caffeine.cache.Cache;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.glasslauncher.mods.landscaped.LandscapedBiomeSource;
import net.glasslauncher.mods.landscaped.LandscapedCompatibleBiome;
import net.glasslauncher.mods.landscaped.LandscapedCompatibleDimension;
import net.glasslauncher.mods.landscaped.ScaledOctaveSimplexSampler;
import net.glasslauncher.mods.landscaped.events.init.LandscapedBiomesInit;
import net.glasslauncher.mods.landscaped.events.init.LandscapedConfig;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;
import java.util.Random;

@Mixin(BiomeSource.class)
public class BiomeSourceMixin implements LandscapedBiomeSource {
    @Unique
    private World world;
    @Unique
    private OctavePerlinNoiseSampler voronoiSmootherSampler;

    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At(value = "NEW", target = "(Ljava/util/Random;I)Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;", ordinal = 0, shift = At.Shift.BEFORE))
    private void init(World par1, CallbackInfo ci) {
        world = par1;
        voronoiSmootherSampler = new OctavePerlinNoiseSampler(new Random(world.getSeed()), 4) {
            private final Int2DoubleOpenHashMap CACHE = new Int2DoubleOpenHashMap();
            {
                CACHE.defaultReturnValue(Double.MIN_VALUE);
            }

            @Override
            public double sample(double x, double y) {

                int key = Objects.hash(x, y);
                double value = CACHE.get(key);
                if (value == Double.MIN_VALUE) {
                    CACHE.put(key, value = super.sample(x, y));
                }
                return value;
            }
        };
    }

    @WrapOperation(method = "<init>(Lnet/minecraft/world/World;)V", at = @At(value = "NEW", target = "(Ljava/util/Random;I)Lnet/minecraft/util/math/noise/OctaveSimplexNoiseSampler;"))
    private OctaveSimplexNoiseSampler scaleBiomes(Random random, int oct, Operation<OctaveSimplexNoiseSampler> original) {
        return new ScaledOctaveSimplexSampler(random, oct, LandscapedConfig.INSTANCE.biomeScale.scale);
    }

    @Inject(method = "getBiomesInArea([Lnet/minecraft/world/biome/Biome;IIII)[Lnet/minecraft/world/biome/Biome;", at = @At(value = "HEAD"), cancellable = true)
    private void test(Biome[] biomes, int x, int z, int width, int depth, CallbackInfoReturnable<Biome[]> cir) {
        cir.setReturnValue(getBiomesForArea(x, z, width, depth, world));
    }

    @Override
    public OctavePerlinNoiseSampler landscaped$getVoronoiSmootherSampler() {
        return voronoiSmootherSampler;
    }

    @Override
    public World landscaped$getWorld() {
        return world;
    }

    @Override
    public void landscaped$setWorld(World world) {
        this.world = world;
    }
}
