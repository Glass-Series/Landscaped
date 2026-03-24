package net.glasslauncher.mods.landscaped;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.modificationstation.stationapi.api.util.Identifier;

public interface LandscapedWorld {
    Identifier[] landscaped$getBiomeIndexToID();

    Object2IntMap<Identifier> landscaped$getBiomeIDToIndex();
}
