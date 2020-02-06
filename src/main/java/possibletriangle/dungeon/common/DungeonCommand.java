package possibletriangle.dungeon.common;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.DungeonChunkGenerator;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.room.Generateable;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;

import java.util.Comparator;
import java.util.Map;

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

    private static Optional<Pair<Integer,Generateable>> roomAt(BlockPos pos, World world) {

        ChunkPos chunk = new ChunkPos(pos);
        DungeonSettings settings = new DungeonSettings();
        Map<Integer, Generateable> rooms = DungeonChunkGenerator.roomsFor(world, chunk);
        int floor = pos.getY() / (DungeonSettings.FLOOR_HEIGHT + 1);

        int nextFloor = rooms.keySet().stream().filter(f -> f <= floor).max(Comparator.comparingInt(a -> a)).orElse(0);
        Generateable room = rooms.get(nextFloor);
        if(room == null) return Optional.empty();
        return Optional.of(new Pair(room, floor));

    }

    private static int getRoom(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();

        BlockPos pos = new BlockPos(source.getPos());
        try {
            pos = BlockPosArgument.getBlockPos(context, "pos");
        } catch (CommandSyntaxException ignored) {}

        World world = source.getWorld();
        roomAt(pos, world).ifPresent(pair -> {
            int floor = pair.getValue();
            Generateable room = pair.getKey();
            StructureMetadata meta = room.getMeta();
            Vec3i size = room.getSize();

            source.sendFeedback(new TranslationTextComponent("command.dungeon.get.room", meta.getDisplay()), false);
            source.sendFeedback(new TranslationTextComponent("command.dungeon.get.floor", floor, settings.floors), false);
            source.sendFeedback(new TranslationTextComponent("command.dungeon.get.size", size.getX(), size.getZ(), size.getY()), false);
            source.sendFeedback(new TranslationTextComponent("command.dungeon.get.palette", "unknown"), false);
            source.sendFeedback(new TranslationTextComponent("command.dungeon.get.weight", meta.getWeight()), false);
        }).orElse(() -> {
            source.sendFeedback(new TranslationTextComponent("command.dungeon.get.no_room"), false);
        });

        return rooms.size();
    }

}
