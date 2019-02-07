package possibletriangle.dungeon.rooms;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.DungeonOptions;
import possibletriangle.dungeon.structures.RoomStructure;

import javax.annotation.Nullable;
import java.util.Random;

@Mod.EventBusSubscriber
public class RoomSpawn extends RoomStructure {

    public static final RoomSpawn SPAWN = new RoomSpawn();

    public RoomSpawn() {
        super("room", "spawn");
    }

    @SubscribeEvent
    public static void onJoined(EntityJoinWorldEvent event) {

        String key = Dungeon.MODID + ":joined";

        if(event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if(!player.getEntityData().getBoolean(key)) {
                respawn(player, event.getWorld());
                player.getEntityData().setBoolean(key, true);
            }
        }
    }

    public static void respawn(EntityPlayer player, World world) {

        int chunkX = world.getSpawnPoint().getX() / 16;
        int chunkZ = world.getSpawnPoint().getZ() / 16;

        BlockPos spawn = new BlockPos(chunkX*16, 0, chunkZ*16).add(SPAWN.safeSpot());

        player.setPosition(spawn.getX(), spawn.getY(), spawn.getZ());
        //player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 100000, 0, true, false));
        player.setGameType(GameType.SPECTATOR);

    }

    @Override
    public BlockPos safeSpot() {
        return new BlockPos(8, 4, 8);
    }

    @Override
    public boolean generateWall() {
        return true;
    }
}
