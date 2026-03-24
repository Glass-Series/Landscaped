package net.glasslauncher.mods.landscaped.events.init;

import net.glasslauncher.mods.landscaped.Landscaped;
import net.glasslauncher.mods.landscaped.blocks.LeafPile;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;

public class LandscapedBlockInit {
    public static LeafPile leafPile;

    @EventListener
    public static void blocks(BlockRegistryEvent event) {
        leafPile = new LeafPile(Landscaped.NAMESPACE.id("leaf_pile"));
    }
}

