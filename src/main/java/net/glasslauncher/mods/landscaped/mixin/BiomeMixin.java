package net.glasslauncher.mods.landscaped.mixin;

import net.glasslauncher.mods.landscaped.events.LandscapedBiomeEvent;
import net.glasslauncher.mods.landscaped.events.init.LandscapedBiomesInit;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.StationAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Biome.class)
public class BiomeMixin {

    @Inject(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/biome/Biome;init()V",
                    shift = At.Shift.AFTER
            )
    )
    private static void stationapi_afterVanillaBiomes(CallbackInfo ci) {
        StationAPI.EVENT_BUS.post(LandscapedBiomeEvent.builder().build());
    }
}
