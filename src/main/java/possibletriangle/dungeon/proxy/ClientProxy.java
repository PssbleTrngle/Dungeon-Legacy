package possibletriangle.dungeon.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.block.tile.ChestTile;
import possibletriangle.dungeon.block.tile.MetadataTile;
import possibletriangle.dungeon.block.tile.render.ChestTESR;
import possibletriangle.dungeon.client.MetadataScreen;
import possibletriangle.dungeon.block.tile.render.MetadataTESR;
import possibletriangle.dungeon.entity.GrenadeEntity;

public class ClientProxy {

    public void openMetaTile(MetadataTile tile) {
        Minecraft.getInstance().displayGuiScreen(new MetadataScreen(tile));
    }
}
