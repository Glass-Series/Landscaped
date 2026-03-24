package net.glasslauncher.mods.landscaped.mixin;

import net.glasslauncher.mods.forested.eventlisteners.init.TreeGenerationInit;
import net.glasslauncher.mods.forested.registries.TreeRegisterEvent;
import net.glasslauncher.mods.forested.registries.TreeRegistry;
import net.glasslauncher.mods.forested.registries.TreeRegistryEntry;
import net.glasslauncher.mods.landscaped.Landscaped;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Namespace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(TreeGenerationInit.class)
public class ExplodeVanillaTreesMixin {

    @Inject(method = "vanillaInit", at = @At(value = "HEAD"), cancellable = true)
    private static void explode(TreeRegisterEvent event, CallbackInfo ci) {
        Landscaped.LOGGER.info("Hijacking vanilla tree values from Forested...");
        Registry.register(TreeRegistry.INSTANCE, Namespace.MINECRAFT.id("oak"), new TreeRegistryEntry(new OakTreeFeature(), Map.of(Biome.FOREST, 1000)));
        Registry.register(TreeRegistry.INSTANCE, Namespace.MINECRAFT.id("big_oak"), new TreeRegistryEntry(new LargeOakTreeFeature(), Map.of(Biome.FOREST, 100, Biome.SEASONAL_FOREST, 200)));
        Registry.register(TreeRegistry.INSTANCE, Namespace.MINECRAFT.id("spruce"), new TreeRegistryEntry(new SpruceTreeFeature(), Map.of(Biome.TAIGA, 100)));
        Registry.register(TreeRegistry.INSTANCE, Namespace.MINECRAFT.id("pine"), new TreeRegistryEntry(new PineTreeFeature(), Map.of(Biome.TAIGA, 100)));
        Registry.register(TreeRegistry.INSTANCE, Namespace.MINECRAFT.id("birch"), new TreeRegistryEntry(new BirchTreeFeature(), Map.of(Biome.FOREST, 100)));
        ci.cancel();
    }
}
