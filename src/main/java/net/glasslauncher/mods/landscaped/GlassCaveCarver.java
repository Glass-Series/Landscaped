package net.glasslauncher.mods.landscaped;

import net.glasslauncher.mods.landscaped.events.init.LandscapedConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.carver.Carver;
import net.modificationstation.stationapi.impl.world.CaveGenBaseImpl;

import java.util.Random;

// I'm not even going to pretend I know what's going on here. Just know that it works far better than vanilla. Thanks deleted reddit user.
public class GlassCaveCarver extends Carver {
    protected void carve(int chunkX, int chunkZ, byte[] blocks, double x, double y, double z) {
        this.carveTunnels(chunkX, chunkZ, blocks, x, y, z, 1.0F + (this.random.nextFloat() * LandscapedConfig.INSTANCE.caveScale.scale) * 6.0F, 0.0F, 0.0F, -1, -1, 0.5, new Random(random.nextLong()));
    }

    public void carveTunnels(
            int chunkX,
            int chunkZ,
            byte[] blocks,
            double x,
            double y,
            double z,
            float baseWidth,
            float yaw,
            float pitch,
            int tunnel,
            int tunnelCount,
            double widthHeightRatio,
            Random var23
    ) {
        double var17 = chunkX * 16 + 8;
        double var19 = chunkZ * 16 + 8;
        float var21 = 0.0F;
        float var22 = 0.0F;
        if (tunnelCount <= 0) {
            int var24 = this.range * 16 - 16;
            tunnelCount = var24 - var23.nextInt(var24 / 4);
        }

        boolean var55 = false;
        if (tunnel == -1) {
            tunnel = tunnelCount / 2;
            var55 = true;
        }

        int var25 = (tunnel < tunnelCount / 4 * 3) ? tunnel + var23.nextInt((tunnelCount - tunnel) / 2) + (tunnelCount - tunnel) / 4 : -1;

        for (boolean var26 = var23.nextInt(6) == 0; tunnel < tunnelCount; tunnel++) {
            double var27 = 1.5 + MathHelper.sin(tunnel * (float) Math.PI / tunnelCount) * baseWidth * 1.0F;
            double var29 = var27 * widthHeightRatio;
            float var31 = MathHelper.cos(pitch);
            float var32 = MathHelper.sin(pitch);
            x += MathHelper.cos(yaw) * var31;
            y += var32;
            z += MathHelper.sin(yaw) * var31;
            if (var26) {
                pitch *= 0.92F;
            } else {
                pitch *= 0.7F;
            }

            pitch += var22 * 0.1F;
            yaw += var21 * 0.1F;
            var22 *= 0.9F;
            var21 *= 0.75F;
            var22 += (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 2.0F;
            var21 += (var23.nextFloat() - var23.nextFloat()) * var23.nextFloat() * 4.0F;
            if (!var55 && tunnel == var25 && baseWidth > 1.0F) {
                this.carveTunnels(chunkX, chunkZ, blocks, x, y, z, (var23.nextFloat() * LandscapedConfig.INSTANCE.caveScale.scale) * 0.5F + 0.5F, yaw - (float) (Math.PI / 2), pitch / 3.0F, tunnel, tunnelCount, 1.0, new Random(var23.nextLong()));
                this.carveTunnels(chunkX, chunkZ, blocks, x, y, z, (var23.nextFloat() * LandscapedConfig.INSTANCE.caveScale.scale) * 0.5F + 0.5F, yaw + (float) (Math.PI / 2), pitch / 3.0F, tunnel, tunnelCount, 1.0, new Random(var23.nextLong()));
                return;
            }

            if (var55 || var23.nextInt(4) != 0) {
                double var33 = x - var17;
                double var35 = z - var19;
                double var37 = tunnelCount - tunnel;
                double var39 = baseWidth + 2.0F + 16.0F;
                if (var33 * var33 + var35 * var35 > (var37 + var39) * (var37 + var39)) {
                    return;
                }

                if (!(x < var17 - 16.0 - var27 * 2.0) && !(z < var19 - 16.0 - var27 * 2.0) && !(x > var17 + 16.0 + var27 * 2.0) && !(z > var19 + 16.0 + var27 * 2.0)) {
                    int var56 = MathHelper.floor(x - var27) - chunkX * 16 - 1;
                    int var34 = MathHelper.floor(x + var27) - chunkX * 16 + 1;
                    int var57 = MathHelper.floor(y - var29) - 1;
                    int var36 = MathHelper.floor(y + var29) + 1;
                    int var58 = MathHelper.floor(z - var27) - chunkZ * 16 - 1;
                    int var38 = MathHelper.floor(z + var27) - chunkZ * 16 + 1;
                    if (var56 < 0) {
                        var56 = 0;
                    }

                    if (var34 > 16) {
                        var34 = 16;
                    }

                    if (var57 < getWorld().getBottomY() + 1) {
                        var57 = getWorld().getBottomY() + 1;
                    }

                    if (var36 > getWorld().getTopY() - 8) {
                        var36 = getWorld().getTopY() - 8;
                    }

                    if (var58 < 0) {
                        var58 = 0;
                    }

                    if (var38 > 16) {
                        var38 = 16;
                    }

                    boolean var59 = false;

                    for (int var40 = var56; !var59 && var40 < var34; var40++) {
                        for (int var41 = var58; !var59 && var41 < var38; var41++) {
                            for (int var42 = var36 + 1; !var59 && var42 >= var57 - 1; var42--) {
                                int var43 = (var40 * 16 + var41) * net.modificationstation.stationapi.api.util.math.MathHelper.smallestEncompassingPowerOfTwo(getWorld().getTopY()) + var42;
                                var43 -= getWorld().getBottomY();
                                if (var42 >= 0 && var42 < 128) {
                                    if (blocks[var43] == Block.FLOWING_WATER.id || blocks[var43] == Block.WATER.id) {
                                        var59 = true;
                                    }

                                    if (var42 != var57 - 1 && var40 != var56 && var40 != var34 - 1 && var41 != var58 && var41 != var38 - 1) {
                                        var42 = var57;
                                    }
                                }
                            }
                        }
                    }

                    if (!var59) {
                        for (int var60 = var56; var60 < var34; var60++) {
                            double var61 = (var60 + chunkX * 16 + 0.5 - x) / var27;

                            for (int var62 = var58; var62 < var38; var62++) {
                                double var44 = (var62 + chunkZ * 16 + 0.5 - z) / var27;
                                int var46 = (var60 * 16 + var62) * net.modificationstation.stationapi.api.util.math.MathHelper.smallestEncompassingPowerOfTwo(getWorld().getTopY()) + var36;
                                var46 -= getWorld().getBottomY();
                                boolean var47 = false;
                                if (var61 * var61 + var44 * var44 < 1.0) {
                                    for (int var48 = var36 - 1; var48 >= var57; var48--) {
                                        double var49 = (var48 + 0.5 - y) / var29;
                                        if (var49 > -0.7 && var61 * var61 + var49 * var49 + var44 * var44 < 1.0) {
                                            byte var51 = blocks[var46];
                                            if (var51 == Block.GRASS_BLOCK.id) {
                                                var47 = true;
                                            }

                                            if (var51 == Block.STONE.id || var51 == Block.DIRT.id || var51 == Block.GRASS_BLOCK.id) {
                                                if (var48 < 10) {
                                                    blocks[var46] = (byte)Block.FLOWING_LAVA.id;
                                                } else {
                                                    blocks[var46] = 0;
                                                    if (var47 && blocks[var46 - 1] == Block.DIRT.id) {
                                                        blocks[var46 - 1] = (byte)Block.GRASS_BLOCK.id;
                                                    }
                                                }
                                            }
                                        }

                                        var46--;
                                    }
                                }
                            }
                        }

                        if (var55) {
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void carve(World world, int startChunkX, int startChunkZ, int chunkX, int chunkZ, byte[] blocks) {
        int var7 = this.random.nextInt(this.random.nextInt(this.random.nextInt(40) + 1) + 1);
        if (this.random.nextInt(15) != 0) {
            var7 = 0;
        }

        for (int var8 = 0; var8 < var7; var8++) {
            double var9 = startChunkX * 16 + this.random.nextInt(16);
            double var11 = this.random.nextInt(this.random.nextInt(120) + 8);
            double var13 = startChunkZ * 16 + this.random.nextInt(16);
            int var15 = 1;
            if (this.random.nextInt(4) == 0) {
                this.carve(chunkX, chunkZ, blocks, var9, var11, var13);
                var15 += this.random.nextInt(4);
            }

            for (int var16 = 0; var16 < var15; var16++) {
                float var17 = this.random.nextFloat() * (float) Math.PI * 2.0F;
                float var18 = (this.random.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float var19 = (this.random.nextFloat() * LandscapedConfig.INSTANCE.caveScale.scale) * 2.0F + this.random.nextFloat();
                this.carveTunnels(chunkX, chunkZ, blocks, var9, var11, var13, var19, var17, var18, 0, 0, 1.0, new Random(random.nextLong()));
            }
        }
    }

    private World getWorld() {
        return ((CaveGenBaseImpl) this).stationapi_getWorld();
    }
}

