package possibletriangle.dungeon.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import possibletriangle.dungeon.common.world.DungeonChunkGenerator;
import possibletriangle.dungeon.common.world.room.Generateable;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;
import possibletriangle.dungeon.helper.Pair;

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

        World world = source.func_197023_e();
        Optional<Pair<Integer, Generateable>> pair = DungeonChunkGenerator.roomAt(pos, world);

        return DungeonChunkGenerator.getSettings(world).map(settings -> {

            if (pair.isPresent()) {
                int floor = pair.get().getFirst();
                Generateable room = pair.get().getSecond();
                StructureMetadata meta = room.getMeta();
                Vec3i size = room.getSize();

                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.room", meta.getDisplay()), false);
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.floor", floor, settings.floors), false);
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.size", size.getX(), size.getZ(), size.getY()), false);
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.palette", "unknown"), false);
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.weight", meta.getWeight()), false);
            } else {
                source.sendFeedback(new TranslationTextComponent("command.dungeon.get.no_room"), false);
            }

            return pair.isPresent() ? 1 : 0;

        }).orElseGet(() ->{

            source.sendFeedback(new TranslationTextComponent("command.dungeon.get.no_dungeon"), false);
            return 0;

        });
    }

}
