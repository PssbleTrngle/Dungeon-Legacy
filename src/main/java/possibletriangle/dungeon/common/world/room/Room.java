package possibletriangle.dungeon.common.world.room;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.common.world.DungeonChunk;

import javax.annotation.Nullable;

public abstract class Room extends ForgeRegistryEntry<Room> {

    public final static IForgeRegistry<Room> REGISTRY = GameRegistry.findRegistry(Room.class);

    public abstract void generate(DungeonChunk chunk, int floor);

}
