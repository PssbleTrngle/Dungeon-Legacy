package possibletriangle.dungeon.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.storage.loot.LootContext;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.WorldDataRooms;
import possibletriangle.dungeon.helper.SpawnData;
import possibletriangle.dungeon.loot.LootManager;
import possibletriangle.dungeon.generator.rooms.RoomData;
import possibletriangle.dungeon.structures.DungeonStructur;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CommandDungeon extends CommandBase {

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + getName() + " [" + String.join(" | ", SubCommand.names()) + "]";
    }

    @Override
    public String getName() {
        return Dungeon.MODID;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {

        if(args.length == 1)
            return getListOfStringsMatchingLastWord(args, SubCommand.names());
        if(args.length > 1 && SubCommand.valueOf(args[0].toUpperCase()) == SubCommand.LOOT)
            return getTabCompletionCoordinate(args, 1, targetPos);

        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if(args.length == 0)
            throw new CommandException("Need to specify an action");

        try {
            switch (SubCommand.valueOf(args[0].toUpperCase())) {

                case QUERY:
                    BlockPos chunk = WorldDataRooms.toChunk(sender.getPosition(), sender.getEntityWorld());
                    RoomData data = WorldDataRooms.atFloor(chunk.getX(), chunk.getY(), chunk.getZ(), sender.getEntityWorld());
                    if(data == null)
                        throw new CommandException("There is no room here");

                    sender.sendMessage(new TextComponentString("You are in a \"" + data.name + "\" at floor "+ chunk.getY() + " [" + data.rotation.name().toLowerCase() + "] [" + data.pallete.getBiomeName() + "]" ));
                    break;

                case RELOAD:
                    DungeonStructur.reloadAll();
                    LootManager.reload();
                    sender.sendMessage(new TextComponentString("Reloaded structures and loottables"));
                    break;

                case SPAWN:
                    if(sender instanceof EntityPlayerMP) {
                        EntityPlayerMP player = (EntityPlayerMP) sender;
                        BlockPos p = SpawnData.getSpawn(player, player.getEntityWorld());
                        if(p != null) {

                            player.connection.setPlayerLocation(p.getX() + 0.5, p.getY(), p.getZ() + 0.5, 0, 0);

                        } else
                            throw  new CommandException("You have not set your dungeon spawn");
                    }
                    break;

                case SPAWNRESET:
                    if(sender instanceof EntityPlayerMP) {
                        EntityPlayerMP player = (EntityPlayerMP) sender;

                        SpawnData.resetSpawn(player.getUniqueID(), player.getEntityWorld());
                        sender.sendMessage(new TextComponentString("Spawnpoint has been reset"));
                    }
                    break;

                case LOOT:

                    if(args.length >= 4) {

                        int luck = args.length >= 5 ? parseInt(args[4]) : 0;
                        int times = args.length >= 6 ? Math.max(1, parseInt(args[5])) : 1;
                        BlockPos pos = parseBlockPos(sender, args, 1, false);
                        TileEntity te = sender.getEntityWorld().getTileEntity(pos);
                        if(te instanceof IInventory) {
                            IInventory i = (IInventory) te;
                            i.clear();
                            for(int x = 0; x < times; x++)
                                LootManager.COMMON.fillInventory(i, new Random(), new LootContext(luck, null, null, null, null, null));
                        } else
                            throw new CommandException("Not an inventory");

                    } else
                        throw new CommandException("No position specified");

                    break;

                default:
                    throw new IllegalArgumentException("");

            }
        } catch (IllegalArgumentException ex) {
            throw new CommandException("\"" + args[0] + "\" is ot a valid action");
        }

    }

    public enum SubCommand {
        QUERY, RELOAD, SPAWN, SPAWNRESET, LOOT;

        public static String[] names() {
            ArrayList<String> names = new ArrayList<>();
            for(SubCommand s : values())
                names.add(s.name().toLowerCase());
            return names.toArray(new String[0]);
        }
    }

}
