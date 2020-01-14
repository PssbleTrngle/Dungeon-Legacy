package possibletriangle.dungeon.common.world.structure;

import com.google.gson.JsonObject;
import com.mojang.datafixers.DataFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MinecartItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DefaultTypeReferences;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.loading.FMLServerLaunchProvider;
import org.apache.commons.io.IOUtils;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.room.Room;
import possibletriangle.dungeon.common.world.room.RoomStructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.Collection;

public class StructureLoader {

    public static void load(IResourceManager manager, Room.Type type, ResourceLocation r) {
        try {
            manager.getAllResources(r).stream().forEach(resource -> {
                try {
                    DungeonStructure structure = read(resource.getInputStream());
                    StructureMetadata metadata = resource.getMetadata(StructureMetadata.SERIALIZER);
                    if(metadata == null) metadata = StructureMetadata.SERIALIZER.deserialize(new JsonObject());
                    Room.register(new RoomStructure(structure), type, metadata);
                } catch (IOException e) {
                    DungeonMod.LOGGER.info("Error on loading file for '{}'", r.toString());
                }
            });
        } catch (IOException ex) {
            DungeonMod.LOGGER.info("Structure '{}' not found", r.toString());
        }
    }

    public static void reload(IResourceManager manager) {
        DungeonMod.LOGGER.info("Reloading structures");

        for(Room.Type type : Room.Type.values()) {

            Collection<ResourceLocation> resources = manager.getAllResourceLocations("structures/" + type.name().toLowerCase(), s -> s.endsWith(".nbt"));
            DungeonMod.LOGGER.info("Found {} structure files for type {}", resources.size(), type.name());

            resources.forEach(r -> load(manager, type, r));
        }
    }

    public static void load(ResourceLocation source) {
        String PATH = "assets/" + source.getNamespace() + "/structures/";
    }

    public static DungeonStructure read(ResourceLocation source) {

        InputStream input = null;
        try {
            File file = new File("structures/", source.getPath() + ".nbt");
            input = new FileInputStream(file);
            return read(input);
        } catch (Throwable ignored) {
            return null;
        } finally {
            IOUtils.closeQuietly(input);
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
