package net.glasslauncher.mods.landscaped.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.glasslauncher.mods.landscaped.ScaledOctavePerlinSampler;
import net.glasslauncher.mods.landscaped.events.init.LandscapedConfig;
import net.minecraft.util.math.noise.OctavePerlinNoiseSampler;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(OverworldChunkGenerator.class)
public class Overworld {
    @WrapOperation(method = "<init>", at = @At(value = "NEW", target = "(Ljava/util/Random;I)Lnet/minecraft/util/math/noise/OctavePerlinNoiseSampler;"))
    private OctavePerlinNoiseSampler scaleBiomes(Random random, int oct, Operation<OctaveSimplexNoiseSampler> original) {
        return new ScaledOctavePerlinSampler(random, oct, LandscapedConfig.INSTANCE.biomeScale.scale);
    }
}
