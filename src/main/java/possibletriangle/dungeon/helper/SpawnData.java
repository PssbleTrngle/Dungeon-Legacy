package possibletriangle.dungeon.helper;

import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
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

import javax.xml.bind.annotation.XmlElementDecl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Mod.EventBusSubscriber
public class SpawnData extends WorldSavedData {

    public static final boolean singleSpawn = true;

    private final HashMap<BlockPos, String> TEAM_MAP = new HashMap<>();
    private final HashMap<BlockPos, UUID> UUID_MAP = new HashMap<>();
    public final ArrayList<BlockPos> GLOBAL = new ArrayList<>();

    public static final String NAME = Dungeon.MODID + ":spawn_data";

    public SpawnData() {
        super(NAME);
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

        if (te != null) {

           if(singleSpawn) {

               ArrayList<BlockPos> remove = new ArrayList<>();
               for(BlockPos pos : DATA.UUID_MAP.keySet())
                   if(DATA.UUID_MAP.get(pos).equals(uuid))
                       remove.add(pos);
               for(BlockPos p : remove)
                   declaimSpawn(p, world);
           }

            DATA.UUID_MAP.put(spawn_pos, uuid);
            String team = updateTeam(uuid, world);

            te.owner = uuid;
            te.lastTeam = team;
            te.markDirty();

        }

    }

    public static void resetSpawn(UUID uuid, World world) {

        SpawnData DATA = get(world);
        ArrayList<BlockPos> remove = new ArrayList<>();
        for(BlockPos pos : DATA.UUID_MAP.keySet()) {
            if (DATA.UUID_MAP.get(pos).equals(uuid))
                remove.add(pos);
        }

        for(BlockPos pos : remove) {
            DATA.UUID_MAP.remove(pos);
            DATA.TEAM_MAP.remove(pos);
        }

    }

    public static void declaimSpawn(BlockPos spawn_pos, World world) {

        SpawnData DATA = get(world);
        DATA.UUID_MAP.remove(spawn_pos);
        DATA.TEAM_MAP.remove(spawn_pos);
        TileEntitySpawn te = ModBlocks.SPAWN.getTE(spawn_pos, world);

        if (te != null) {
            te.owner = null;
            te.lastTeam = null;
            te.markDirty();
        }

    }

    public static String getOwnerTeam(BlockPos pos, World world) {

        SpawnData DATA = get(world);
        String team = DATA.TEAM_MAP.get(pos);

        return team;

    }

    private static String updateTeam(UUID uuid, World world) {

        SpawnData DATA = get(world);
        if(uuid != null) {
            EntityPlayer player = world.getPlayerEntityByUUID(uuid);
            if(player != null && player.getTeam() != null) {
                for(BlockPos pos : DATA.UUID_MAP.keySet())
                    if(DATA.UUID_MAP.get(pos).equals(uuid)) {
                    DATA.TEAM_MAP.put(pos, player.getTeam().getName());
                    return player.getTeam().getName();
                }
            }
        }

        return null;

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

    public static BlockPos getSpawn(EntityPlayer player, World world) {

        ArrayList<BlockPos> remove = new ArrayList<>();
        SpawnData DATA = get(world);
        BlockPos p = null;

        for(BlockPos pos : DATA.UUID_MAP.keySet())
            if(DATA.UUID_MAP.get(pos).equals(player.getUniqueID())) {
                TileEntitySpawn te = ModBlocks.SPAWN.getTE(pos, world);
                if(te != null)
                    p = pos;
                else
                    remove.add(pos);
            }

        if(player.getTeam() != null)
            for(BlockPos pos : DATA.TEAM_MAP.keySet())
                if(DATA.TEAM_MAP.get(pos).equals(player.getTeam().getName())) {
                    TileEntitySpawn te = ModBlocks.SPAWN.getTE(pos, world);
                    if(te != null)
                        p = pos;
                    else
                        remove.add(pos);
                    }

        for(BlockPos pos : remove) {
            declaimSpawn(pos, world);
        }

        ArrayList<BlockPos> rg = new ArrayList<>();
        for(BlockPos pos : DATA.GLOBAL) {
            TileEntitySpawn te = ModBlocks.SPAWN.getTE(pos, world);
            if(te == null || !te.global)
                rg.add(pos);
        }

        DATA.GLOBAL.removeAll(rg);

        if(p == null && !DATA.GLOBAL.isEmpty()) {

            return DATA.GLOBAL.get(0);
        }

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
                BlockPos pos = new BlockPos(b.getInteger("x"), b.getInteger("y"), b.getInteger("z"));
                String key = b.getString("key");

                TEAM_MAP.put(pos, key);

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
        for (BlockPos pos : TEAM_MAP.keySet()) {

            NBTTagCompound b = new NBTTagCompound();
            b.setString("key", TEAM_MAP.get(pos));
            b.setInteger("x", pos.getX());
            b.setInteger("y", pos.getY());
            b.setInteger("z", pos.getZ());

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

        BlockPos spawn = SpawnData.getSpawn(event.player, event.player.getEntityWorld());
        event.player.setSpawnPoint(spawn, true);
        event.player.sendMessage(new TextComponentString("Respawned"));

    }

}
