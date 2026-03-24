package net.glasslauncher.mods.landscaped.events;

import lombok.experimental.SuperBuilder;
import net.glasslauncher.mods.landscaped.LandscapedBiome;
import net.glasslauncher.mods.landscaped.LandscapedBiomeRegistry;
import net.mine_diver.unsafeevents.Event;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.registry.Registries;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;

@SuperBuilder
public class LandscapedBiomeEvent extends Event {
    public void register(Biome biome) {
        if (((LandscapedBiome) biome).landscaped$getIdentifier() == null) {
            throw new RuntimeException("Biome " + biome.getClass() + " has no identifier set! Use landscaped$setIdentifier!");
        }
        Registry.register(LandscapedBiomeRegistry.INSTANCE, ((LandscapedBiome) biome).landscaped$getIdentifier(), biome);
    }
}
