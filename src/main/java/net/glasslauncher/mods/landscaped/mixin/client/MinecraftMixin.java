package net.glasslauncher.mods.landscaped.mixin.client;

import net.glasslauncher.mods.landscaped.BiomeDistributor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"))
    private void inject(World world, String message, PlayerEntity player, CallbackInfo ci) {
        BiomeDistributor.updateDistributor(world);
    }
}
