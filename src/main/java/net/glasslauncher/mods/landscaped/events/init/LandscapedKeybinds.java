package net.glasslauncher.mods.landscaped.events.init;

import net.glasslauncher.mods.landscaped.BiomeVisualiser;
import net.glasslauncher.mods.landscaped.Landscaped;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.KeyBinding;
import net.modificationstation.stationapi.api.client.event.keyboard.KeyStateChangedEvent;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingEvent;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

public class LandscapedKeybinds {
    public static final KeyBinding EXPORT_WORLD = new KeyBinding("Export World", Keyboard.KEY_O);

    @EventListener
    public static void keypress(KeyStateChangedEvent event) {
        if (Minecraft.INSTANCE.world != null && Keyboard.getEventKey() == EXPORT_WORLD.code && Keyboard.getEventKeyState()) {
            Landscaped.LOGGER.info("Exporting world biomes...");
            BiomeVisualiser.exportWorld(Minecraft.INSTANCE.world);
        }
    }

    @EventListener
    public static void keys(KeyBindingRegisterEvent event) {
        event.keyBindings.add(EXPORT_WORLD);
    }
}
