package net.glasslauncher.mods.landscaped.events.init;

import net.glasslauncher.mods.landscaped.Landscaped;
import net.glasslauncher.mods.landscaped.LandscapedWorldBiomeIndexSyncPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;

public class LandscapedNetworking {

    @EventListener
    public static void types(PacketRegisterEvent event) {
        event.register(Landscaped.NAMESPACE.id("biome_index_sync"), LandscapedWorldBiomeIndexSyncPacket.TYPE);
    }
}
