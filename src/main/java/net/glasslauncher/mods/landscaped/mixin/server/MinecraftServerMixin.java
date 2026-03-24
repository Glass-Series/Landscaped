package net.glasslauncher.mods.landscaped.mixin.server;

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

    @Inject(method = "loadWorld", at = @At(value = "NEW", target = "(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/world/storage/WorldStorage;Ljava/lang/String;IJ)Lnet/minecraft/world/ServerWorld;", shift = At.Shift.AFTER))
    private void inject(WorldStorageSource storageSource, String worldDir, long seed, CallbackInfo ci) {
        BiomeDistributor.updateDistributor(worlds[0]);
    }
}
