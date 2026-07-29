package net.glasslauncher.mods.landscaped.mixin.stationapi;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.mods.landscaped.LandscapedBiome;
import net.glasslauncher.mods.landscaped.LandscapedChunk;
import net.glasslauncher.mods.landscaped.LandscapedPacketWithChunkData;
import net.glasslauncher.mods.landscaped.LandscapedWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.impl.packet.FlattenedChunkDataS2CPacket;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@Mixin(FlattenedChunkDataS2CPacket.class)
public class FlattenedChunkDataS2CPacketMixin implements LandscapedPacketWithChunkData {
    @Unique
    private final int[] biomes = new int[256];

    @Environment(EnvType.SERVER)
    @Inject(method = "<init>(Lnet/minecraft/world/World;II)V", at = @At(value = "TAIL"))
    private void e(World world, int chunkX, int chunkZ, CallbackInfo ci, @Local(name = "chunk") FlattenedChunk chunk) {
        Biome[] retrievedBiomes = ((LandscapedChunk) chunk).landscaped$getBiomes();
        Object2IntMap<Identifier> biomeIDsToIndex = ((LandscapedWorld) world).landscaped$getBiomeIDToIndex();
        for (int index = 0; index < 256; index++) {
            if (retrievedBiomes[index] == null) {
                System.out.println("Bad biome on packet build??");
                retrievedBiomes[index] = Biome.FOREST;
            }
            biomes[index] = biomeIDsToIndex.getInt(((LandscapedBiome) retrievedBiomes[index]).landscaped$getIdentifier());
        }
    }

    @Inject(method = "write", at = @At(value = "INVOKE", target = "Ljava/io/DataOutputStream;writeInt(I)V", ordinal = 1, shift = At.Shift.AFTER))
    private void customBiomes(DataOutputStream out, CallbackInfo ci) throws IOException {
        for(int i : biomes) {
            out.write(i);
        }
    }

    @Inject(method = "read", at = @At(value = "INVOKE", target = "Ljava/io/DataInputStream;readInt()I", ordinal = 1, shift = At.Shift.AFTER))
    private void e(DataInputStream in, CallbackInfo ci) throws IOException {
        for(int i = 0; i < biomes.length; i++) {
            biomes[i] = in.read();
        }
    }

    @ModifyConstant(method = "size", constant = @Constant(intValue = 16))
    private int resize(int constant) {
        return constant + biomes.length;
    }

    @Override
    public int[] landscaped$getBiomes() {
        return biomes;
    }
}
