package net.glasslauncher.mods.landscaped.events.init;

import net.glasslauncher.mods.gcapi3.api.ConfigEntry;
import net.glasslauncher.mods.gcapi3.api.ConfigRoot;
import net.glasslauncher.mods.landscaped.BiomeDistributor;
import net.glasslauncher.mods.landscaped.biomedististributor.EquatorialBiomeDistributor;
import net.glasslauncher.mods.landscaped.biomedististributor.PatchyBiomeDistributor;
import net.minecraft.world.World;

import java.util.function.Function;
import java.util.function.Supplier;

public class LandscapedConfig {
    @ConfigRoot(value = "config", visibleName = "Landscaped Config")
    public static final Config INSTANCE = new Config();

    public static class Config {
        private Config() {}

        @ConfigEntry(name = "Biome Scale")
        public BiomeScale biomeScale = BiomeScale.TWO;

        @ConfigEntry(name = "Land Scale")
        public BiomeScale landScale = BiomeScale.TWO;

        @ConfigEntry(name = "Cave Scale")
        public CaveScale caveScale = CaveScale.TWO;

        @ConfigEntry(name = "Biome Distributor")
        public BiomeDistributorOption biomeDistributor = BiomeDistributorOption.EQUATORIAL;
    }

    public enum BiomeScale {
        ONE(1, "1x (vanilla)"),
        TWO(2, "2x"),
        FOUR(4, "4x"),
        EIGHT(8, "8x"),
        SIXTEEN(16, "16x (experimental)"),
        ;

        public final int scale;
        public final String name;

        BiomeScale(int scale, String name) {
            this.scale = scale;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum CaveScale {
        ONE(1, "1x (vanilla)"),
        TWO(2, "2x (tunnels)"),
        FOUR(4, "4x (small caverns)"),
        EIGHT(8, "8x (big caverns)"),
        SIXTEEN(16, "16x (cavern world)"),
        ;

        public final int scale;
        public final String name;

        CaveScale(int scale, String name) {
            this.scale = scale;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum BiomeDistributorOption {
        EQUATORIAL("Equatorial (Vintage Story-like)", EquatorialBiomeDistributor::new),
        PATCHY("Patchy (vanilla)", PatchyBiomeDistributor::new),
        ;

        public final String name;
        public final Function<World, BiomeDistributor> distributor;

        BiomeDistributorOption(String name, Function<World, BiomeDistributor> distributor) {
            this.name = name;
            this.distributor = distributor;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
