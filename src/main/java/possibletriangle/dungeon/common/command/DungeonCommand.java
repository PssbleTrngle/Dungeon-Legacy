package possibletriangle.dungeon.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import possibletriangle.dungeon.common.block.Palette;
import possibletriangle.dungeon.helper.Pair;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.DungeonChunkGenerator;
import possibletriangle.dungeon.common.world.room.Generateable;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;

import java.util.Optional;

public class DungeonCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(DungeonMod.ID)
                        .then(PaletteCommand.register())
                        .then(RoomCommand.register())
        );
    }

}
