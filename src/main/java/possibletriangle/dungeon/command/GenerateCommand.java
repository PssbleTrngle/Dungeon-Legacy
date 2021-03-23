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

public class GenerateCommand {

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("generate")
                .then(Commands.argument("from", BlockPosArgument.blockPos())
                        .then(Commands.argument("to", BlockPosArgument.blockPos())
                                .executes(GenerateCommand::generate)));
    }

    private static BlockPos posOrDefault(CommandContext<CommandSource> context, String id, BlockPos def) throws CommandSyntaxException {
        try {
            return BlockPosArgument.getBlockPos(context, id);
        } catch (IllegalArgumentException ex) {
            return def;
        }
    }

    private static int generate(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CommandSource source = context.getSource();

        BlockPos from = posOrDefault(context, "from", new BlockPos(source.getPos()));
        BlockPos to = posOrDefault(context, "to", from);

        World world = source.getWorld();

        DungeonChunkGenerator.getSettings(world).map(settings -> {



        });

        return 1;
    }

}
