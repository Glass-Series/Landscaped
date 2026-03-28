package net.glasslauncher.mods.landscaped.mixin;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.glasslauncher.mods.landscaped.*;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.light.LightUpdate;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.storage.WorldStorage;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

@Mixin(World.class)
public class WorldMixin implements LandscapedWorld {
    @Shadow @Final public Dimension dimension;
    @Shadow public boolean isRemote;
    @Unique
    Identifier[] biomeIndexToID;
    @Unique
    Object2IntMap<Identifier> biomeIDToIndex = new Object2IntOpenHashMap<>();

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;Lnet/minecraft/world/dimension/Dimension;J)V", at = @At("RETURN"))
    private void test(WorldStorage worldStorage, String name, Dimension dimension, long seed, CallbackInfo ci) {
        initBiomeMap(worldStorage);
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/world/dimension/Dimension;)V", at = @At("RETURN"))
    private void test(World world, Dimension dimension, CallbackInfo ci) {
        initBiomeMap(world.getWorldStorage());
    }

    @Inject(method = "<init>(Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;JLnet/minecraft/world/dimension/Dimension;)V", at = @At("RETURN"))
    private void init(WorldStorage worldStorage, String name, long seed, Dimension dimension, CallbackInfo ci) {
        initBiomeMap(worldStorage);
    }

    @Unique
    private void initBiomeMap(WorldStorage worldStorage) {
        if (isRemote) {
            return; // Server world, we let the server tell us what exists here.
        }
        File propsFile = worldStorage.getWorldPropertiesFile("landscapedbiomes");
        if (propsFile.exists()) {
            try {
                // Any biomes that don't exist when a chunk loads will get reset to whatever the biome sampler decides.
                String[] ids = new Gson().fromJson(new JsonReader(new FileReader(propsFile)), String[].class);
                biomeIndexToID = new Identifier[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    biomeIndexToID[i] = Identifier.of(ids[i]);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // Now to populate the map with any new values and save it
        Set<Identifier> biomes = new HashSet<>(List.of(biomeIndexToID != null ? biomeIndexToID : new Identifier[0]));
        for (Biome biome : LandscapedBiomeRegistry.INSTANCE.stream().toList()) {
            biomes.add(((LandscapedBiome) biome).landscaped$getIdentifier());
        }
        biomeIndexToID = biomes.toArray(new Identifier[0]);
        for (int i = 0; i < biomeIndexToID.length; i++) {
            biomeIDToIndex.put(biomeIndexToID[i], i);
        }
        try (FileWriter writer = new FileWriter(propsFile)) {
            writer.write(new Gson().toJson(biomeIDToIndex.keySet().stream().map(Identifier::toString).toArray()));
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Identifier[] landscaped$getBiomeIndexToID() {
        return biomeIndexToID;
    }

    @Override
    public Object2IntMap<Identifier> landscaped$getBiomeIDToIndex() {
        return biomeIDToIndex;
    }

}
