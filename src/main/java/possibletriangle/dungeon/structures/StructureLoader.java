package possibletriangle.dungeon.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.io.IOUtils;
import possibletriangle.dungeon.generator.ChunkPrimerRotateable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class StructureLoader {

    public static final String baseFolder = "structures/";

    public static void read(ResourceLocation source, List<Template.BlockInfo> blocks, List<NBTTagCompound> entites) {

        InputStream inputstream = null;
        try
        {
            File file = new File(baseFolder, source.getResourcePath() + ".nbt");
            inputstream = new FileInputStream(file);
            read(inputstream, blocks, entites);
        }
        catch (Throwable ignored)
        {
        }
        finally
        {
            IOUtils.closeQuietly(inputstream);
        }
    }

    public static void read(NBTTagCompound compound, List<Template.BlockInfo> blocks, List<NBTTagCompound> entites) {

        NBTTagList nbttaglist = compound.getTagList("size", 3);
        BlockPos size = new BlockPos(nbttaglist.getIntAt(0), nbttaglist.getIntAt(1), nbttaglist.getIntAt(2));

        NBTTagList nbt_palette = compound.getTagList("palette", 10);
        NBTTagList nbt_blocks = compound.getTagList("blocks", 10);

        HashMap<Integer, IBlockState> palette = new HashMap<>();

        for (int i = 0; i < nbt_palette.tagCount(); ++i)
        {
            palette.put(i, NBTUtil.readBlockState(nbt_palette.getCompoundTagAt(i)));
        }


        for (int j = 0; j < nbt_blocks.tagCount(); ++j)
        {
            NBTTagCompound nbttagcompound = nbt_blocks.getCompoundTagAt(j);
            NBTTagList pos_tag = nbttagcompound.getTagList("pos", 3);
            BlockPos blockpos = new BlockPos(pos_tag.getIntAt(0), pos_tag.getIntAt(1), pos_tag.getIntAt(2));
            IBlockState iblockstate = palette.get(nbttagcompound.getInteger("state"));
            NBTTagCompound block_nbt;

            block_nbt = nbttagcompound.getCompoundTag("nbt");
            blocks.add(new Template.BlockInfo(blockpos, iblockstate, block_nbt));
        }

        NBTTagList nbt_entities = compound.getTagList("entities", 10);
        for (int j = 0; j < nbt_entities.tagCount(); ++j) {

            NBTTagCompound nbttagcompound = nbt_entities.getCompoundTagAt(j);
            entites.add(nbttagcompound);

        }

    }

    public static void read(InputStream stream, List<Template.BlockInfo> blocks, List<NBTTagCompound> entites) throws IOException
    {
        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(stream);

        if (!nbttagcompound.hasKey("DataVersion", 99))
        {
            nbttagcompound.setInteger("DataVersion", 500);
        }

        read(FMLCommonHandler.instance().getDataFixer().process(FixTypes.STRUCTURE, nbttagcompound), blocks, entites);

    }

    public static Template.BlockInfo rotate(Template.BlockInfo info, Rotation rotation, double[] center) {

        IBlockState state = info.blockState.withRotation(rotation);
        return new Template.BlockInfo(ChunkPrimerRotateable.rotate(info.pos, rotation, center), state, info.tileentityData);

    }

}
