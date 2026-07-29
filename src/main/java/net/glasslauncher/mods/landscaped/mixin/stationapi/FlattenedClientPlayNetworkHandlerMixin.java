package net.glasslauncher.mods.landscaped.mixin.stationapi;

import com.llamalad7.mixinextras.sugar.Local;
import net.glasslauncher.mods.landscaped.LandscapedBiomeRegistry;
import net.glasslauncher.mods.landscaped.LandscapedChunk;
import net.glasslauncher.mods.landscaped.LandscapedPacketWithChunkData;
import net.glasslauncher.mods.landscaped.LandscapedWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.modificationstation.stationapi.impl.client.network.FlattenedClientPlayNetworkHandler;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlattenedClientPlayNetworkHandler.class)
public class FlattenedClientPlayNetworkHandlerMixin {
    @Inject(method = "onMapChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;populateHeightMapOnly()V", shift = At.Shift.AFTER))
    private void e(FlattenedChunkDataS2CPacket packet, CallbackInfo ci, @Local(name = "chunk") Chunk chunk) {
        Biome[] biomes = new Biome[256];
        int[] biomeIndexes = ((LandscapedPacketWithChunkData) packet).landscaped$getBiomes();
        for (int i = 0; i < 256; i++) {
            biomes[i] = LandscapedBiomeRegistry.INSTANCE.get(((LandscapedWorld) chunk.world).landscaped$getBiomeIndexToID()[biomeIndexes[i]]);
            if (biomes[i] == null) {
                System.err.println("Bad biome on packet read??");
                biomes[i] = Biome.FOREST;
            }
        }
        ((LandscapedChunk) chunk).landscaped$setBiomes(biomes);
    }
}
