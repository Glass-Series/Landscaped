package net.glasslauncher.mods.landscaped.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import net.glasslauncher.mods.landscaped.BiomeDistributor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.storage.WorldStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Shadow public ServerWorld[] worlds;

    @Inject(method = "loadWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ServerWorld;addEventListener(Lnet/minecraft/world/event/listener/GameEventListener;)V", shift = At.Shift.BEFORE))
    private void inject(WorldStorageSource storageSource, String worldDir, long seed, CallbackInfo ci, @Local int var6) {
        BiomeDistributor.updateDistributor(worlds[var6]);
    }
}
