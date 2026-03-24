package net.glasslauncher.mods.landscaped.mixin.stationapi;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.glasslauncher.mods.landscaped.LandscapedBiome;
import net.glasslauncher.mods.landscaped.LandscapedBiomeRegistry;
import net.glasslauncher.mods.landscaped.LandscapedChunk;
import net.glasslauncher.mods.landscaped.LandscapedWorld;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.world.FlattenedWorldManager;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlattenedWorldManager.class)
public class FlattenedWorldManagerMixin {

    @Inject(method = "loadChunk", at = @At("RETURN"))
    private static void yeet(World world, NbtCompound chunkTag, CallbackInfoReturnable<Chunk> cir, @Local(name = "chunk") FlattenedChunk chunk) {
        if (chunkTag.contains("Biomes")) {
            int[] biomeIndexes = chunkTag.getIntArray("Biomes");
            Biome[] biomes = new Biome[256];
            for (int index = 0; index < biomeIndexes.length; index++) {
                biomes[index] = LandscapedBiomeRegistry.INSTANCE.get(((LandscapedWorld) world).landscaped$getBiomeIndexToID()[biomeIndexes[index]]);
                if (biomes[index] == null) {
                    System.out.println("Bad biome on load??");
                    biomes[index] = Biome.FOREST;
                }
            }
            ((LandscapedChunk) chunk).landscaped$setBiomes(biomes);
            return;
        }

        System.out.println("Old world?");
        // TODO: Warn the user that they're loading an old world, and that biomes will move around
        // Okay so this is an old world, fun. Hope they don't mind the biomes getting fucky :)
        ((LandscapedChunk) chunk).landscaped$setBiomes(world.dimension.biomeSource.getBiomesInArea(chunk.x << 4, chunk.z << 4, 16, 16));
    }

    @Inject(method = "saveChunk", at = @At("RETURN"))
    private static void yeet(FlattenedChunk chunk, World world, NbtCompound chunkTag, CallbackInfo ci) {
        int[] biomeIndexes = new int[256];
        Biome[] biomes = ((LandscapedChunk) chunk).landscaped$getBiomes();
        Object2IntMap<Identifier> biomeIDsToIndex = ((LandscapedWorld) world).landscaped$getBiomeIDToIndex();
        for (int index = 0; index < 256; index++) {
            if (biomes[index] == null) {
                System.out.println("Bad biome on save??");
                biomes[index] = Biome.FOREST;
            }
            biomeIndexes[index] = biomeIDsToIndex.getInt(((LandscapedBiome) biomes[index]).landscaped$getIdentifier());
        }
        chunkTag.put("Biomes", biomeIndexes);
    }
}
