package net.glasslauncher.mods.landscaped;

/**
 * Be aware that oceans are *not* a biome in this version.
 */
public enum TemperatureCategory {
    COLD(0, 0.2f),
    COOL(0.2f, 0.4f),
    TEMPERATE(0.4f, 0.6f),
    WARM(0.6f, 0.8f),
    HOT(0.8f, 1),
    ;

    public final float minTemp;
    public final float maxTemp;

    TemperatureCategory(float minTemp, float maxTemp) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }
}
