package net.glasslauncher.mods.landscaped;

import lombok.SneakyThrows;
import net.glasslauncher.mods.landscaped.stapiplaceholders.TemplateManagedPacket;
import net.minecraft.network.NetworkHandler;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;

public class LandscapedWorldBiomeIndexSyncPacket extends TemplateManagedPacket<LandscapedWorldBiomeIndexSyncPacket> {
    public static final PacketType<LandscapedWorldBiomeIndexSyncPacket> TYPE = PacketType.builder(true, false, LandscapedWorldBiomeIndexSyncPacket::new).blocking().build();

    private World world;

    public LandscapedWorldBiomeIndexSyncPacket() {
    }

    public LandscapedWorldBiomeIndexSyncPacket(World world) {
        this.world = world;
    }

    @Override
    @SneakyThrows
    public void write(TrackingOutputStream outputStream) {
        Identifier[] ids = ((LandscapedWorld) world).landscaped$getBiomeIndexToID();
        outputStream.writeInt(ids.length);
        for (Identifier identifier : ids) {
            outputStream.writeUTF(identifier.toString());
        }
    }

    @Override
    @SneakyThrows
    public void read(DataInputStream stream) {
        Identifier[] ids = new Identifier[stream.readInt()];
        Landscaped.LOGGER.info("Loading biome array of size {} from server", ids.length);
        for (int i = 0; i < ids.length; i++) {
            ids[i] = Identifier.of(stream.readUTF());
        }
        Landscaped.clientBiomeArrayHolder = ids;
    }

    @Override
    public void apply(NetworkHandler networkHandler) {
    }

    @Override
    public @NotNull PacketType<LandscapedWorldBiomeIndexSyncPacket> getType() {
        return TYPE;
    }
}
