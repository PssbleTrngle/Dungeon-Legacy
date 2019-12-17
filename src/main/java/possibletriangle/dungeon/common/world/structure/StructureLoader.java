package possibletriangle.dungeon.common.world.structure;

import com.mojang.datafixers.DataFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DefaultTypeReferences;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.loading.FMLServerLaunchProvider;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StructureLoader {

    public static final String baseFolder = "structures/";

    public static DungeonStructure read(ResourceLocation source) {

        InputStream inputstream = null;
        try
        {
            File file = new File(baseFolder, source.getPath() + ".nbt");
            inputstream = new FileInputStream(file);
            return read(inputstream);

        } catch (Throwable ignored) {
            return null;
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    public static DungeonStructure read(InputStream stream) throws IOException {
        CompoundNBT nbt = CompressedStreamTools.readCompressed(stream);

        if (!nbt.contains("DataVersion", 99)) {
            nbt.putInt("DataVersion", 500);
        }

        DungeonStructure structure = new DungeonStructure();
        DataFixer fixed = Minecraft.getInstance().getDataFixer();
        structure.read(NBTUtil.update(fixed, DefaultTypeReferences.STRUCTURE, nbt, nbt.getInt("DataVersion")));
        return structure;

    }

}
