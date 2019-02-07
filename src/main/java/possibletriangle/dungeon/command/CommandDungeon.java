package possibletriangle.dungeon.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import possibletriangle.dungeon.Dungeon;
import possibletriangle.dungeon.generator.WorldDataRooms;
import possibletriangle.dungeon.structures.DungeonStructur;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
            return SubCommand.names();

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
                    int floor = WorldDataRooms.floor(sender.getPosition().getY(), sender.getEntityWorld());
                    ResourceLocation room = WorldDataRooms.atY(sender.getPosition().getX() / 16 + 1, sender.getPosition().getY(), sender.getPosition().getZ() / 16 + 1, sender.getEntityWorld());
                    if(room == null)
                        throw new CommandException("There is no room here");

                    sender.sendMessage(new TextComponentString("You are in a \"" + room.toString() + "\" atY floor " + floor));
                    break;

                case RELOAD:
                    DungeonStructur.reloadAll();
                    sender.sendMessage(new TextComponentString("Reloaded structures"));
                    break;

                    default:
                        throw new IllegalArgumentException("");

            }
        } catch (IllegalArgumentException ex) {
            throw new CommandException("\"" + args[0] + "\" is ot a valid action");
        }

    }

    public enum SubCommand {
        QUERY, RELOAD;

        public static List<String> names() {
            ArrayList<String> names = new ArrayList<>();
            for(SubCommand s : values())
                names.add(s.name().toLowerCase());
            return names;
        }
    }

}
