package net.glasslauncher.mods.landscaped.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class LeafPile extends TemplateBlock {
    public LeafPile(Identifier identifier) {
        super(identifier, Material.LEAVES);
        setTranslationKey(identifier);
        maxY = 0.1;
        soundGroup = DIRT_SOUND_GROUP;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public void neighborUpdate(World world, int x, int y, int z, int id) {
        super.neighborUpdate(world, x, y, z, id);

        if (!world.getBlockState(x, y - 1, z).getBlock().isSolidFace(world, x, y - 1, z, Direction.UP.getId())) {
            world.setBlock(x, y, z, 0);
        }
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int z) {
        return world.getBlockState(x, y - 1, z).getBlock().isSolidFace(world, x, y - 1, z, Direction.UP.getId()) && world.getBlockId(x, y, z) == 0;
    }

    @Override
    public boolean isSolidFace(BlockView blockView, int x, int y, int z, int face) {
        return false;
    }
}
