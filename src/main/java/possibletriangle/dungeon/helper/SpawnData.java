package possibletriangle.dungeon.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.block.ModBlocks;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;
import possibletriangle.dungeon.generator.WorldDataRooms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber
public class SpawnData extends WorldSavedData {

    public static final boolean singleSpawn = true;

    private final HashMap<UUID, String> TEAM_MAP = new HashMap<>();
    private final HashMap<BlockPos, UUID> UUID_MAP = new HashMap<>();
    public final ArrayList<BlockPos> GLOBAL = new ArrayList<>();

    public static final String NAME = Dungeon.MODID + ":spawn_data";

    public SpawnData() {
        this(NAME);
    }

    public SpawnData(String name) {
        super(name);
    }

    public static SpawnData get(World world) {
        MapStorage storage = world.getMapStorage() == null ? world.getPerWorldStorage() : world.getMapStorage();
        SpawnData instance = (SpawnData) storage.getOrLoadData(SpawnData.class, NAME);

        if (instance == null) {
            instance = new SpawnData();
            storage.setData(NAME, instance);
        }
        return instance;
    }

    public static void claimSpawn(BlockPos spawn_pos, UUID uuid, World world) {

        SpawnData DATA = get(world);
        TileEntitySpawn te = ModBlocks.SPAWN.getTE(spawn_pos, world);
        BlockPos chunk = WorldDataRooms.toChunk(spawn_pos, world);

        if (te != null) {

           if(singleSpawn) {

               ArrayList<BlockPos> remove = new ArrayList<>();
               for(BlockPos pos : DATA.UUID_MAP.keySet())
                   if (!pos.equals(chunk) && DATA.UUID_MAP.get(pos).equals(uuid)) {
                       Dungeon.LOGGER.info("Remove");
                       remove.add(pos);
                    }
               for(BlockPos p : remove)
                   declaimSpawn(p, world);
           }

            DATA.UUID_MAP.put(chunk, uuid);
            updateTeam(uuid, world);
            DATA.markDirty();

        } else
            Dungeon.LOGGER.error("Can not claim that room");


    }

    public static void resetSpawn(UUID uuid, World world) {

        Dungeon.LOGGER.info("Reset Spawn");

        BlockPos chunk = getSpawnChunk(uuid, world);
        if(chunk != null)
            declaimSpawn(chunk, world);

    }

    public static void declaimSpawn(BlockPos chunk, World world) {

        Dungeon.LOGGER.info("Declaimed Spawn");
        SpawnData DATA = get(world);
        DATA.UUID_MAP.remove(chunk);
        DATA.markDirty();

    }

    public static String getOwnerTeam(BlockPos chunk, World world) {

        SpawnData DATA = get(world);
        UUID uuid = DATA.UUID_MAP.get(chunk);
        if(uuid == null) return null;
        return DATA.TEAM_MAP.get(uuid);

    }

    private static String updateTeam(UUID uuid, World world) {

        if(uuid == null)
            return null;

        EntityPlayer player = world.getPlayerEntityByUUID(uuid);
        if(player == null)
            return null;

        SpawnData DATA = get(world);
        if(player.getTeam() == null) {
            DATA.TEAM_MAP.remove(uuid);
            DATA.markDirty();
            return null;
        } else {
            String team = player.getTeam().getName();
            DATA.TEAM_MAP.put(uuid, team);
            DATA.markDirty();
            return team;
        }

    }

    public static UUID getOwner(BlockPos pos, World world) {

        SpawnData DATA = get(world);
        UUID uuid = DATA.UUID_MAP.get(pos);
        updateTeam(uuid, world);

        return uuid;

    }

    public static boolean hasOwner(BlockPos pos, World world) {
        return getOwner(pos, world) != null || getOwnerTeam(pos, world) != null;
    }

    public static BlockPos getSpawnChunk(UUID uuid, World world) {

        SpawnData DATA = get(world);
        ArrayList<BlockPos> remove = new ArrayList<>();
        BlockPos p = null;

        for(BlockPos pos : DATA.UUID_MAP.keySet())
            if(DATA.UUID_MAP.get(pos).equals(uuid)) {
                if(hasSpawnBlock(pos, world))
                    p = pos;
                else
                    remove.add(pos);
            }

        for(BlockPos pos : remove) {
            declaimSpawn(pos, world);
        }

        DATA.markDirty();

        return p;

    }

    private static void updateGlobals(World world) {

        SpawnData DATA = get(world);
        ArrayList<BlockPos> rg = new ArrayList<>();
        for(BlockPos pos : DATA.GLOBAL) {
            TileEntitySpawn te = ModBlocks.SPAWN.getTE(pos, world);
            if(te == null || !te.global)
                rg.add(pos);
        }

        DATA.GLOBAL.removeAll(rg);
        DATA.markDirty();

    }

    public static BlockPos getSpawnBlock(EntityPlayer player, World world) {
        BlockPos chunk = getSpawnChunk(player, world);
        return getSpawnBlock(chunk, world);
    }

    public static BlockPos getSpawnBlock(BlockPos chunk, World world) {
        if(chunk == null) return null;
        return new BlockPos(chunk.getX() * 16 + 8, chunk.getY()*WorldDataRooms.getFloorHeight(world) + 3, chunk.getZ() * 16 + 8);
    }

    public static boolean hasSpawnBlock(BlockPos chunk, World world) {
        return getSpawnBlock(chunk, world) != null;
    }

    public static BlockPos getSpawnChunk(EntityPlayer player, World world) {

        SpawnData DATA = get(world);
        BlockPos p = getSpawnChunk(player.getUniqueID(), world);

        if(p != null)
            return p;

        updateGlobals(world);

        if(player.getTeam() != null)
            for(BlockPos chunk : DATA.UUID_MAP.keySet()) {
                UUID uuid = DATA.UUID_MAP.get(chunk);
                String team = DATA.TEAM_MAP.get(uuid);
                if(team != null && player.getTeam().getName().equals(team))
                    return chunk;
            }

        if(!DATA.GLOBAL.isEmpty()) {
            return WorldDataRooms.toChunk(DATA.GLOBAL.get(0), world);
        }

        DATA.markDirty();
        return p;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        UUID_MAP.clear();
        NBTTagList list_uuid = compound.getTagList(NAME + "_uuid", 0);
        for (NBTBase base : list_uuid) {
            if (base instanceof NBTTagCompound) {

                NBTTagCompound b = (NBTTagCompound) base;
                BlockPos pos = new BlockPos(b.getInteger("x"), b.getInteger("y"), b.getInteger("z"));
                String key = b.getString("key");

                if(!key.equals(""))
                    UUID_MAP.put(pos, UUID.fromString(key));

            }
        }

        TEAM_MAP.clear();
        NBTTagList list_team = compound.getTagList(NAME + "_team", 0);
        for (NBTBase base : list_team) {
            if (base instanceof NBTTagCompound) {

                NBTTagCompound b = (NBTTagCompound) base;
                UUID uuid = UUID.fromString(b.getString("uuid"));
                String team = b.getString("team");

                TEAM_MAP.put(uuid, team);

            }
        }

        GLOBAL.clear();
        NBTTagList list_global = compound.getTagList(NAME + "_global", 0);
        for (NBTBase base : list_global) {
            if (base instanceof NBTTagCompound) {

                NBTTagCompound b = (NBTTagCompound) base;
                BlockPos pos = new BlockPos(b.getInteger("x"), b.getInteger("y"), b.getInteger("z"));

                GLOBAL.add(pos);

            }
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        NBTTagList list_uiud = new NBTTagList();
        for (BlockPos pos : UUID_MAP.keySet()) {

            NBTTagCompound b = new NBTTagCompound();
            b.setString("key", UUID_MAP.get(pos).toString());
            b.setInteger("x", pos.getX());
            b.setInteger("y", pos.getY());
            b.setInteger("z", pos.getZ());

            list_uiud.appendTag(b);

        }
        compound.setTag(NAME + "_team", list_uiud);

        NBTTagList list_team = new NBTTagList();
        for (UUID uuid : TEAM_MAP.keySet()) {

            NBTTagCompound b = new NBTTagCompound();
            b.setString("team", TEAM_MAP.get(uuid));
            b.setString("uuid", uuid.toString());

            list_team.appendTag(b);

        }
        compound.setTag(NAME + "_uuid", list_team);

        NBTTagList list_global = new NBTTagList();
        for (BlockPos pos : GLOBAL) {

            NBTTagCompound b = new NBTTagCompound();
            b.setInteger("x", pos.getX());
            b.setInteger("y", pos.getY());
            b.setInteger("z", pos.getZ());

            list_global.appendTag(b);

        }
        compound.setTag(NAME + "_global", list_global);

        return compound;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDeath(PlayerEvent.PlayerRespawnEvent event) {

        BlockPos spawn = SpawnData.getSpawnChunk(event.player, event.player.getEntityWorld());
        event.player.setSpawnPoint(spawn, true);
        event.player.sendMessage(new TextComponentString("Respawned"));

    }

}
