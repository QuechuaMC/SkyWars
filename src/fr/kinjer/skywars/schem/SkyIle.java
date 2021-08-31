package fr.kinjer.skywars.schem;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;

import fr.kinjer.skywars.SkyWars;
 
public class SkyIle extends ChunkGenerator {
 
    private final CuboidClipboard schematic;
 
    public SkyIle(SkyWars main) throws DataException, IOException {
    	System.out.println(main.getDataFolder().getAbsolutePath());
        schematic = SchematicFormat.MCEDIT.load(new File(main.getDataFolder(), "/schems/-31.155.5.skyile.schematic"));
    }
 
    @Override
    public byte[][] generateBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes) {
        byte[][] result = new byte[world.getMaxHeight() / 16][];
        //
        int xRegion;
        if (cx % 2 == 0) {
            xRegion = 0;
        } else {
            xRegion = 1;
        }
        //
        int zRegion;
        if (cz % 2 == 0) {
            zRegion = 0;
        } else {
            zRegion = 1;
        }
        //
        for (int x = 0; x < 16; x++) {
            int xBlock = (xRegion * 16) + x;
            //
            for (int z = 0; z < 16; z++) {
                //
                int zBlock = (zRegion * 16) + z;
                //
                for (int y = 0; y < world.getMaxHeight(); y++) {
                    try {
                        BaseBlock block = schematic.getPoint(new BlockVector(xBlock, y, zBlock));
                        setBlock(result, x, y, z, (byte) block.getType());
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        //
                    }
                }
            }
        }
        return result;
    }
 
    private void setBlock(byte[][] result, int x, int y, int z, byte blkid) {
        if (result[y >> 4] == null) {
            result[y >> 4] = new byte[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
    }
}