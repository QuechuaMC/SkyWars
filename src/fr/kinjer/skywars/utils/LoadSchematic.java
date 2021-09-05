package fr.kinjer.skywars.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.world.DataException;

import fr.kinjer.skywars.schem.Schematic;

public class LoadSchematic {
	
	public static void pasteSchematic(World world, Location loc, Schematic schematic)
    {
        byte[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();

        short length = schematic.getLenght();
        short width = schematic.getWidth();
        short height = schematic.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    Block block = new Location(world, x + loc.getX(), y + loc.getY(), z + loc.getZ()).getBlock();
//                    System.out.println(blocks[index]);
                    if(blocks[index] >= 0)
                    	block.setTypeIdAndData(blocks[index], blockData[index], true);                    
                }
            }
        }
        System.out.println(loc.getBlockX()+ " " + loc.getBlockY()+ " " + loc.getBlockZ());
    }
 
    public static Schematic loadSchematic(File file) throws IOException
    {
        FileInputStream stream = new FileInputStream(file);
        NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream));
        CompoundTag schematicTag = (CompoundTag) nbtStream.readNamedTag().getTag();
//        if (!schematicTag.getName().equals("Schematic")) {
//            throw new IllegalArgumentException("Tag \"Schematic\" does not exist or is not first");
//        }
 
        Map<String, Tag> schematic = schematicTag.getValue();
        if (!schematic.containsKey("Blocks")) {
            throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
        }
 
        short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
        short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
        short height = getChildTag(schematic, "Height", ShortTag.class).getValue();
 
        String materials = getChildTag(schematic, "Materials", StringTag.class).getValue();
        if (!materials.equals("Alpha")) {
            throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
        }
 
        byte[] blocks = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
        byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class).getValue();
        System.out.println(width + " " + length + " " + height + " " + blocks.length + " " + blockData.length);
        return new Schematic(blocks, blockData, width, length, height);
    }
 
    /**
    * Get child tag of a NBT structure.
    *
    * @param items The parent tag map
    * @param key The name of the tag to get
    * @param expected The expected type of the tag
    * @return child tag casted to the expected type
    * @throws DataException if the tag does not exist or the tag is not of the
    * expected type
    */
    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException
    {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }

}
