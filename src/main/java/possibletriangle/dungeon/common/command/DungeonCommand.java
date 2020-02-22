package possibletriangle.dungeon.common.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import possibletriangle.dungeon.DungeonMod;

public class DungeonCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(DungeonMod.ID)
                        .then(PaletteCommand.register())
                        .then(RoomCommand.register())
        );
    }

}
