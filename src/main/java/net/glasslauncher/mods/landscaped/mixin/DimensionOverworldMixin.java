package net.glasslauncher.mods.landscaped.mixin;

import net.glasslauncher.mods.landscaped.LandscapedCompatibleDimension;
import net.minecraft.world.dimension.OverworldDimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(OverworldDimension.class)
public class DimensionOverworldMixin implements LandscapedCompatibleDimension {
}
