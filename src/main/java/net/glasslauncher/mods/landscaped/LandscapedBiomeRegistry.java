package net.glasslauncher.mods.landscaped;

import com.mojang.serialization.Lifecycle;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.registry.RegistryKey;
import net.modificationstation.stationapi.api.registry.SimpleRegistry;

public class LandscapedBiomeRegistry extends SimpleRegistry<Biome> {
    public static final RegistryKey<LandscapedBiomeRegistry> KEY = RegistryKey.ofRegistry(Landscaped.NAMESPACE.id("biomes"));
    public static final LandscapedBiomeRegistry INSTANCE = new LandscapedBiomeRegistry();

    public LandscapedBiomeRegistry() {
        super(KEY, Lifecycle.stable());
    }
}
