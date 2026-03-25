package net.glasslauncher.mods.landscaped.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.glasslauncher.mods.landscaped.GlassCaveCarver;
import net.glasslauncher.mods.landscaped.LandscapedCompatibleBiome;
import net.glasslauncher.mods.landscaped.LandscapedCompatibleDimension;
import net.glasslauncher.mods.landscaped.events.init.LandscapedBlockInit;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(OverworldChunkGenerator.class)
public class OverworldGenMixin {

    @Shadow private Carver cave;

    @Shadow private World world;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void hijack(World seed, long par2, CallbackInfo ci) {
        if (!(world.dimension instanceof LandscapedCompatibleDimension)) {
            return;
        }
        cave = new GlassCaveCarver();
        ((CaveGenBaseImpl) cave).stationapi_setWorld(world);
    }

    @WrapOperation(method = "decorate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/Feature;generate(Lnet/minecraft/world/World;Ljava/util/Random;III)Z"))
    private boolean test(Feature instance, World world, Random random, int x, int y, int z, Operation<Boolean> original, @Local Biome biome) {
        if (!(world.dimension instanceof LandscapedCompatibleDimension)) {
            return original.call(instance, world, random, x, y, z);
        }

        boolean didGen = original.call(instance, world, random, x, y, z);
        int radius = 6;
        if (didGen && biome.canRain()) {
            for (int genX = -radius; genX <= radius; genX++) {
                for (int genZ = -radius; genZ <= radius; genZ++) {
                    if (genX * genX + genZ * genZ <= radius * radius) {
                        int top = getTopYForLeaves(world, x + genX, z + genZ);
                        if (top != -1 && random.nextInt(10) == 0) {
                            world.setBlock(x + genX, top, z + genZ, LandscapedBlockInit.leafPile.id);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Unique
    private int getTopYForLeaves(World world, int x, int z) {
        int top = world.getTopY();
        while (top > 0) {
            if (LandscapedBlockInit.leafPile.canPlaceAt(world, x, top, z)) {
                BlockState state = world.getBlockState(x, top - 1, z);
                if (state.getMaterial() != Material.LEAVES) {
                    return top;
                }
            }
            top--;
        }
        return -1;
    }
}
