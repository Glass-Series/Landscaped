package net.glasslauncher.mods.landscaped;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BiomeVisualiser {
    public static void exportWorld(World world) {
        int viewDistance = 4096 - (Minecraft.INSTANCE.options.viewDistance * 1024) ;
        int ySize = viewDistance * 3;
//        int size = 4096;
        int xSize = viewDistance;
        BufferedImage image = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        int lastPercent = 0;

        for (int x = 0; x < xSize; x++) {
            double percent = Math.ceil(((float)x / xSize) * 100);
            if (percent >= lastPercent + 5) {
                Landscaped.LOGGER.info("{}%...", (int) percent);
                lastPercent += 5;
            }
            for (int y = 0; y < ySize; y++) {
                Biome biome = world.dimension.biomeSource.getBiome(x - (xSize / 2) + (int) Minecraft.INSTANCE.player.x, y - (ySize / 2) + (int) Minecraft.INSTANCE.player.y);
                graphics.setColor(getColorFromInt(biome.grassColor));
                graphics.fillRect(x, y, 1, 1);
            }
        }

        try {
            ImageIO.write(image, "png", new File("biome_map.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exportDistribution() {
        exportDistribution(0.001d);
    }

    public static void exportDistribution(double resolution) {
        BufferedImage image = generateDistribution(resolution);
        try {
            ImageIO.write(image, "png", new File("biome_distribution.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage generateDistribution(double resolution) {
        int biomeMapHeight = (int) Math.ceil(1d / resolution);
        BufferedImage image = new BufferedImage((int) Math.ceil(1d / resolution), biomeMapHeight + (LandscapedBiomeRegistry.INSTANCE.size() * 16) + 4, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        int duplicatesIterator;

        for (double temperature = 0; temperature < 1d; temperature += resolution) {
            int imageX = (int)Math.round(temperature / resolution);
            duplicatesIterator = (temperature % (resolution * 2d)) == 0 ? 0 : 1;
            for (double downfall = 0; downfall < 1d; downfall += resolution) {
                int imageY = (int)(Math.round(downfall / resolution));
                Biome[] biomes = BiomeDistributor.getCurrentDistributor().getBiomesForValues(temperature, downfall);
                Biome biome = biomes[duplicatesIterator % biomes.length];
                graphics.setColor(getColorFromInt(biome.grassColor));
                graphics.fillRect(imageX, imageY, 1, 1);
                duplicatesIterator++;
            }
        }
        int offset = 0;
        List<Biome> biomes = new ArrayList<>(LandscapedBiomeRegistry.INSTANCE.stream().toList());
        biomes.sort((a, b) -> Float.compare(((LandscapedBiome) a).landscaped$getTemperature(), ((LandscapedBiome) a).landscaped$getTemperature()));

        for (Biome biome : biomes) {
            graphics.setColor(getColorFromInt(biome.grassColor));
            graphics.fillRect(2, biomeMapHeight + (offset * 16) + 2, 16, 16);
            graphics.setColor(Color.BLACK);
            graphics.drawString(biome.name, 20, biomeMapHeight + (offset * 16) + 14);
            offset++;
        }
        return image;
    }

    public static Color getColorFromInt(int color) {
        float red = (float)(color >> 16 & 255) / 255;
        float green = (float)(color >> 8 & 255) / 255;
        float blue = (float)(color & 255) / 255;
        return new Color(red, green, blue);
    }
}
