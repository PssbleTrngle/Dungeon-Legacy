package possibletriangle.dungeon.common.world.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.mojang.datafixers.DataFixer;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DefaultTypeReferences;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.room.Structures;
import possibletriangle.dungeon.common.world.room.StructureType;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class StructureLoader extends ReloadListener<List<DungeonStructure>> {

    private final StructureType type;
    public StructureLoader(StructureType type) {
        this.type = type;
    }

    /**
     * Loads all structures in the data folder
     * Called on server starting
     * @param manager The servers resource manager
     */
    @Override
    protected List<DungeonStructure> prepare(IResourceManager manager, IProfiler profiler) {

        Collection<ResourceLocation> resources = manager.getAllResourceLocations("structures/" + type.folder(), s -> s.endsWith(".nbt"));
        DungeonMod.LOGGER.info("Found {} structure files for type {}", resources.size(), type.getRegistryName());

        return resources.stream()
                .map(r -> load(manager, r))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Override
    protected void apply(List<DungeonStructure> list, IResourceManager manager, IProfiler profiler) {
        list.forEach(structure ->  Structures.register(structure, type));
    }

    /**
     * Loads a single structure file and its metadata
     * @param manager The servers resource manager
     * @param path The path pointing to the structures files
     */
    private static Optional<List<DungeonStructure>> load(IResourceManager manager, ResourceLocation path) {
        try {
            return manager.getAllResources(path).stream().map(resource -> {
                try {

                    return Optional.of(readStructure(resource));

                } catch (IOException e) {
                    DungeonMod.LOGGER.error("Error on loading file for '{}'", path.toString());
                    return Optional.empty();
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        } catch (IOException ex) {
            DungeonMod.LOGGER.error("Structure '{}' not found", path.toString());
            return Optional.empty();
        }
    }

    /**
     * @param resource The IResource pointing to the structures files
     * @return A DungeonStructure instance with the specified metadata reading from the given IResource
     */
    private static DungeonStructure readStructure(IResource resource) throws IOException {

        StructureMetadata meta = resource.getMetadata(StructureMetadata.SERIALIZER);
        if(meta == null) {
            String filename = resource.getLocation().getPath();
            meta = new StructureMetadata(1F, filename);
        }

        CompoundNBT nbt = CompressedStreamTools.readCompressed(resource.getInputStream());

        if (!nbt.contains("DataVersion", 99)) {
            nbt.putInt("DataVersion", 500);
        }

        DungeonStructure structure = new DungeonStructure(meta);
        DataFixer fixed = Minecraft.getInstance().getDataFixer();
        structure.read(NBTUtil.update(fixed, DefaultTypeReferences.STRUCTURE, nbt, nbt.getInt("DataVersion")));
        return structure;

    }

}
