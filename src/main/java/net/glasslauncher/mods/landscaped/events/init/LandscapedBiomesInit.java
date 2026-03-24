package net.glasslauncher.mods.landscaped.events.init;

import net.glasslauncher.mods.gcapi3.api.CharacterUtils;
import net.glasslauncher.mods.landscaped.BiomeVisualiser;
import net.glasslauncher.mods.landscaped.Landscaped;
import net.glasslauncher.mods.landscaped.LandscapedBiome;
import net.glasslauncher.mods.landscaped.events.LandscapedBiomeEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.event.world.biome.BiomeRegisterEvent;
import net.modificationstation.stationapi.api.util.Identifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LandscapedBiomesInit {

    @EventListener
    public static void modifyBiomes(BiomeRegisterEvent event) {
        Biome.RAINFOREST.setLeavesColor((b, x, y) -> CharacterUtils.getIntFromColour(new Color(0, 255, 0)));
        Biome.RAINFOREST.setGrassColorProvider((b, x, y) -> CharacterUtils.getIntFromColour(new Color(0, 255, 0)));

        ((LandscapedBiome) Biome.TUNDRA)
                .landscaped$setTemperature(0.0f)
                .landscaped$setDownfall(0.8f)
                .landscaped$setIdentifier(Identifier.of("tundra"));
        ((LandscapedBiome) Biome.TAIGA)
                .landscaped$setTemperature(0.2f)
                .landscaped$setDownfall(0.8f)
                .landscaped$setIdentifier(Identifier.of("taiga"));
        ((LandscapedBiome) Biome.FOREST)
                .landscaped$setTemperature(0.5f)
                .landscaped$setDownfall(0.5f)
                .landscaped$setIdentifier(Identifier.of("forest"));
        ((LandscapedBiome) Biome.SEASONAL_FOREST)
                .landscaped$setTemperature(0.6f)
                .landscaped$setDownfall(0.5f)
                .landscaped$setIdentifier(Identifier.of("seasonal_forest"));
        ((LandscapedBiome) Biome.SWAMPLAND)
                .landscaped$setTemperature(0.6f)
                .landscaped$setDownfall(1.0f)
                .landscaped$setIdentifier(Identifier.of("swampland"));
        ((LandscapedBiome) Biome.SHRUBLAND)
                .landscaped$setTemperature(0.6f)
                .landscaped$setDownfall(0.3f)
                .landscaped$setIdentifier(Identifier.of("shrubland"));
        ((LandscapedBiome) Biome.PLAINS)
                .landscaped$setTemperature(0.6f)
                .landscaped$setDownfall(0.4f)
                .landscaped$setIdentifier(Identifier.of("plains"));
        ((LandscapedBiome) Biome.RAINFOREST)
                .landscaped$setTemperature(0.8f)
                .landscaped$setDownfall(0.8f)
                .landscaped$setIdentifier(Identifier.of("rainforest"));
        ((LandscapedBiome) Biome.SAVANNA)
                .landscaped$setTemperature(0.8f)
                .landscaped$setDownfall(0.2f)
                .landscaped$setIdentifier(Identifier.of("savanna"));
        ((LandscapedBiome) Biome.DESERT)
                .landscaped$setTemperature(1.0f)
                .landscaped$setDownfall(0.0f)
                .landscaped$setIdentifier(Identifier.of("desert"));
    }

    @EventListener()
    public static void initBiomes(LandscapedBiomeEvent event) {
        List<Biome> biomes = new ArrayList<>();
        Biome lastBiome = null;

        for (int i = 0; i < Biome.BIOMES.length; i++) {
            Biome biome = Biome.BIOMES[i];
            if (((LandscapedBiome) biome).landscaped$getIdentifier() == null) {
                return;
            }
            if (lastBiome == null) {
                lastBiome = biome;
                biomes.add(lastBiome);
                continue;
            }
            if (lastBiome == biome) {
                continue;
            }
            if (biomes.contains(biome)) {
                lastBiome = biome;
                continue;
            }
            biomes.add(biome);
            lastBiome = biome;
        }
        biomes.forEach(event::register);

        BiomeVisualiser.exportDistribution();

        Landscaped.LOGGER.info("Custom biome init finished.");
    }
}
