package net.glasslauncher.mods.landscaped.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.glasslauncher.mods.landscaped.*;
import net.glasslauncher.mods.landscaped.events.init.LandscapedConfig;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Random;

@Mixin(OverworldChunkGenerator.class)
public class OverworldChunkGeneratorMixin {
    @Shadow private World world;

    @Shadow public Biome[] biomes;

    @WrapOperation(method = "<init>", at = @At(value = "NEW", target = "(Ljava/util/Random;I)Lnet/minecraft/util/math/noise/OctavePerlinNoiseSampler;"))
    private OctavePerlinNoiseSampler scaleBiomes(Random random, int oct, Operation<OctavePerlinNoiseSampler> original) {
        return new ScaledOctavePerlinSampler(random, oct, LandscapedConfig.INSTANCE.biomeScale.scale);
    }

    @Inject(method = "getChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;method_1781()Lnet/minecraft/world/biome/source/BiomeSource;", ordinal = 1, shift = At.Shift.BEFORE))
    private void test(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir, @Local Chunk chunk) {
        ((LandscapedChunk) chunk).landscaped$setBiomes(Arrays.copyOf(biomes, 256));
    }

    @Inject(method = "buildSurfaces", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextDouble()D", ordinal = 0, shift = At.Shift.BEFORE))
    private void test(int chunkX, int chunkZ, byte[] blocks, Biome[] biomes, CallbackInfo ci, @Local(index = 8) int inChunkX, @Local(index = 9) int inChunkZ, @Local LocalRef<Biome> biome) {
        biome.set(((LandscapedBiomeSource) world.method_1781()).getBiomeAt((chunkX * 16) + inChunkZ, (chunkZ * 16) + inChunkX, world, null));
    }

    @WrapOperation(method = "decorate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/BiomeSource;getBiome(II)Lnet/minecraft/world/biome/Biome;"))
    private Biome test2(BiomeSource instance, int x, int z, Operation<Biome> original) {
        return ((LandscapedBiomeSource) instance).getBiomeAt(x, z, world, world.getChunkFromPos(x, z));
    }
}
