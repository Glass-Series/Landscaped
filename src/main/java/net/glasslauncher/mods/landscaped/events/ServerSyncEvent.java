package net.glasslauncher.mods.landscaped.events;

import net.glasslauncher.mods.landscaped.LandscapedWorldBiomeIndexSyncPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.server.event.network.PlayerPacketHandlerSetEvent;

public class ServerSyncEvent {

    @EventListener
    public void sync(PlayerPacketHandlerSetEvent event) {
        event.player.networkHandler.sendPacket(new LandscapedWorldBiomeIndexSyncPacket(event.player.world));
    }
}
