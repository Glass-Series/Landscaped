package net.glasslauncher.mods.landscaped.mixin.server;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.play.ChatMessagePacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class DebugMixin {
    @Shadow
    private ServerPlayerEntity player;

    @Inject(method = "onChatMessage", at = @At("HEAD"))
    private void debug(ChatMessagePacket par1, CallbackInfo ci) {
        player.sendMessage(player.world.dimension.biomeSource.getBiome((int) player.x, (int) player.z).name);
    }
}
