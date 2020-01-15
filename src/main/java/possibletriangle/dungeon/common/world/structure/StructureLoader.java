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

    /**
     * Loads all structures in the data folder
     * Called on server starting
     * @param manager The servers resource manager
     */
    public static void reload(IResourceManager manager) {
        DungeonMod.LOGGER.info("Reloading structures...");

        for(Structures.Type type : Structures.Type.values()) {

            Collection<ResourceLocation> resources = manager.getAllResourceLocations("structures/" + type.name().toLowerCase(), s -> s.endsWith(".nbt"));
            DungeonMod.LOGGER.info("Found {} structure files for type {}", resources.size(), type.name());

            resources.forEach(r -> load(manager, type, r));
        }
    }

    /**
     * Loads a single structure file and its metadata and registeres them
     * @param manager The servers resource manager
     * @param type The type the structure should be registed as
     * @param name The path pointing to the structures files
     */
    private static void load(IResourceManager manager, Structures.Type type, ResourceLocation path) {
        try {
            manager.getAllResources(path).stream().forEach(resource -> {
                try {

                    DungeonStructure structure = readStructure(resource);
                    Room.register(structure, type);

                } catch (IOException e) {
                    DungeonMod.LOGGER.error("Error on loading file for '{}'", path.toString());
                }
            });
        } catch (IOException ex) {
            DungeonMod.LOGGER.error("Structure '{}' not found", path.toString());
        }
    }

    /**
     * @param resource The IResource pointing to the structures files
     * @return A DungeonStructure instance with the specified metadata reading from the given IResource
     */
    private static DungeonStructure readStructure(IResource resource) throws IOException {

        StructureMetadata meta = resource.getMetadata(StructureMetadata.SERIALIZER);
        if(meta == null) meta = StructureMetadata.getDefault();

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
