package net.glasslauncher.mods.landscaped.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.glasslauncher.mods.landscaped.LandscapedBiomeSource;
import net.glasslauncher.mods.landscaped.LandscapedCompatibleBiome;
import net.glasslauncher.mods.landscaped.LandscapedCompatibleDimension;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldDecoratorImpl.class)
public class WorldDecoratorImplMixin {

    @Inject(method = "decorate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBottomY()I", shift = At.Shift.BEFORE))
    private static void test(World world, int cx, int cz, CallbackInfo ci, @Local(name = "biome") LocalRef<Biome> biome, @Local(name = "x") int x, @Local(name = "z") int z) {
        biome.set(((LandscapedBiomeSource) world.method_1781()).getBiomeAt(x, z, world, world.getChunk(cx, cz)));
    }

    @Inject(method = "decorate", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getFeatures()Ljava/util/List;", shift = At.Shift.BEFORE, ordinal = 0))
    private static void test2(World world, int cx, int cz, CallbackInfo ci, @Local(name = "biome") LocalRef<Biome> biome, @Local(name = "x1") int x1, @Local(name = "z1") int z1) {
        biome.set(((LandscapedBiomeSource) world.method_1781()).getBiomeAt(x1 + 8, z1 + 8, world, world.getChunk(cx, cz)));
    }
}
