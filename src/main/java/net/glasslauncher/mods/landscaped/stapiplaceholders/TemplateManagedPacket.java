package net.glasslauncher.mods.landscaped.stapiplaceholders;

import net.minecraft.network.packet.Packet;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 * A simple packet template that implements ManagedPacket and a simple size tracker so you don't have to do it yourself.
 * @param <P> Your packet class.
 */
public abstract class TemplateManagedPacket<P extends Packet & ManagedPacket<P>> extends Packet implements ManagedPacket<P> {
    private static final TrackingOutputStream TRACKER = new TrackingOutputStream();

    @Override
    public void write(DataOutputStream stream) {
        TRACKER.reset(stream);
        write(TRACKER);
    }

    @Override
    public int size() {
        return TRACKER.size();
    }

    /**
     * Implement your mod's packet writing here.
     */
    public abstract void write(TrackingOutputStream outputStream);

    /**
     * A basic OutputStream wrapper that can be easily reset and reused for tracking written data sizes.
     */
    public static class TrackingOutputStream extends DataOutputStream {

        public TrackingOutputStream() {
            super(null);
        }

        public TrackingOutputStream(OutputStream out) {
            super(out);
        }

        public void reset(OutputStream out) {
            written = 0;
            this.out = out;
        }
    }
}