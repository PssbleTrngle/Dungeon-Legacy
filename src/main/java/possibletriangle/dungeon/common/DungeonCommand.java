package possibletriangle.dungeon.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.*;
import net.minecraft.command.impl.SetBlockCommand;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.structure.MineshaftPieces;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonChunkGenerator;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.room.Generateable;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;

public class DungeonCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
          Commands.literal(DungeonMod.MODID)
            .then(Commands.literal("room")
                .then(Commands.literal("get")
                    .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(DungeonCommand::getRoom)
                ).executes(DungeonCommand::getRoom))
            )
        );
    }

    private static int getRoom(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();

        BlockPos pos = new BlockPos(source.getPos());
        try {
            pos = BlockPosArgument.getBlockPos(context, "pos");
        } catch (CommandSyntaxException ignored) {}

        ChunkPos chunk = new ChunkPos(pos);
        World world = source.getWorld();
        DungeonSettings settings = new DungeonSettings();

        Map<Integer, Generateable> rooms = DungeonChunkGenerator.roomsFor(settings, chunk, world.getSeed());
        int floor = pos.getY() / (DungeonSettings.FLOOR_HEIGHT + 1);

        source.sendFeedback(new StringTextComponent("Found " + rooms.size() + " rooms at " + chunk.toString()), true);

        int nextFloor = rooms.keySet().stream().filter(f -> f <= floor).max(Comparator.comparingInt(a -> a)).orElse(0);
        Generateable room = rooms.get(nextFloor);

        if (room != null) {
            source.sendFeedback(new StringTextComponent("Room on floor " + nextFloor + ":"), true);
            source.sendFeedback(new StringTextComponent("Name: " + room.getMeta().display), true);
            source.sendFeedback(new StringTextComponent("Weight: " + room.getMeta().weight), true);
            source.sendFeedback(new StringTextComponent("Size: " + room.getSize().toString()), true);
            source.sendFeedback(new StringTextComponent("Actual Size: " + room.getActualSize().toString()), true);
        }

        return rooms.size();
    }

}
