package possibletriangle.dungeon.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import possibletriangle.dungeon.util.Pair;
import possibletriangle.dungeon.world.generator.DungeonChunkGenerator;
import possibletriangle.dungeon.world.structure.IStructure;
import possibletriangle.dungeon.world.structure.metadata.StructureMetadata;

import java.util.Optional;

public class RoomCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("room")
            .then(Commands.literal("get")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(RoomCommand::getRoom)
                ).executes(RoomCommand::getRoom));
    }

    private static int getRoom(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();

        BlockPos pos = new BlockPos(source.getPos());
        try {
            pos = BlockPosArgument.getBlockPos(context, "pos");
        } catch (CommandSyntaxException ignored) {}

        World world = source.getWorld();
        Optional<Pair<Integer, IStructure>> pair = DungeonChunkGenerator.roomAt(pos, world);

        return DungeonChunkGenerator.getSettings(world).map(settings -> {

            if (pair.isPresent()) {
                int floor = pair.get().getFirst();
                IStructure room = pair.get().getSecond();
                StructureMetadata meta = room.getMeta();
                Vector3i size = room.getSize();

                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.room", meta.getDisplay()), false);
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.floor", floor, settings.floors), false);
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.size", size.getX(), size.getZ(), size.getY()), false);
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.palette", "unknown"), false);
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.weight", meta.getWeight()), false);
            } else {
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.no_room"), false);
            }

            return pair.isPresent() ? 1 : 0;

        }).orElseGet(() -> {

            source.sendFeedback(new TranslationTextComponent("command.dungeon.get.no_dungeon"), false);
            return 0;

        });
    }

}
