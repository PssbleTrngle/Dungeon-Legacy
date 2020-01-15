package possibletriangle.dungeon.common.world.structure;

import com.google.gson.JsonObject;
import com.mojang.datafixers.DataFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DefaultTypeReferences;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.room.Structures;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class StructureLoader {

    private static void load(IResourceManager manager, Structures.Type type, ResourceLocation r) {
        try {
            manager.getAllResources(r).stream().forEach(resource -> {
                try {

                    StructureMetadata meta = resource.getMetadata(StructureMetadata.SERIALIZER);
                    if(meta == null) meta = StructureMetadata.SERIALIZER.deserialize(new JsonObject());
                    DungeonStructure structure = read(resource.getInputStream(), meta);

                    Room.register(structure, type);

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

        for(Structures.Type type : Structures.Type.values()) {

            Collection<ResourceLocation> resources = manager.getAllResourceLocations("structures/" + type.name().toLowerCase(), s -> s.endsWith(".nbt"));
            DungeonMod.LOGGER.info("Found {} structure files for type {}", resources.size(), type.name());

            resources.forEach(r -> load(manager, type, r));
        }
    }

    /*
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
    */

    public static DungeonStructure read(InputStream stream, StructureMetadata meta) throws IOException {
        CompoundNBT nbt = CompressedStreamTools.readCompressed(stream);

        if (!nbt.contains("DataVersion", 99)) {
            nbt.putInt("DataVersion", 500);
        }

        DungeonStructure structure = new DungeonStructure(meta);
        DataFixer fixed = Minecraft.getInstance().getDataFixer();
        structure.read(NBTUtil.update(fixed, DefaultTypeReferences.STRUCTURE, nbt, nbt.getInt("DataVersion")));
        return structure;

    }

}
