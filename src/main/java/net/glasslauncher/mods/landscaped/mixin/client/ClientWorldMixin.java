package net.glasslauncher.mods.landscaped.mixin.client;

import net.glasslauncher.mods.landscaped.Landscaped;
import net.glasslauncher.mods.landscaped.LandscapedWorld;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin implements LandscapedWorld {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void biomes(ClientNetworkHandler seed, long dimensionId, int par3, CallbackInfo ci) {
        System.out.println("set biomes");
        landscaped$setBiomeIndexToID(Landscaped.clientBiomeArrayHolder);
    }
}
