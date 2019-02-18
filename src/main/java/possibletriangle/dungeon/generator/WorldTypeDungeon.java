package possibletriangle.dungeon.generator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiCustomizeWorldScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorFlat;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.Mod;
import possibletriangle.dungeon.Dungeon;

import java.util.Random;

public class WorldTypeDungeon extends WorldType {

    public static final String NAME = "Dungeon";

    private final DungeonOptions options;
    public WorldTypeDungeon() {
        super(NAME);
        this.options = new DungeonOptions();
    }

    @Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
        return new ChunkGeneratorDungeon(options, world, new Random(world.getSeed()));
    }

    @Override
    public boolean isCustomizable() {
        return true;
    }

    @Override
    public void onCustomizeButton(Minecraft mc, GuiCreateWorld guiCreateWorld) {
        mc.displayGuiScreen(new GuiDungeon(guiCreateWorld, options));
    }

    /*
    @Override
    public BiomeProvider getBiomeProvider(World world) {
        return new BiomeProviderDungeon(world);
    }
    */

}
