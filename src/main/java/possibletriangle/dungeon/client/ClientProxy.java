package possibletriangle.dungeon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.CommonProxy;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.entity.GrenadeEntity;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = DungeonMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt) {
        ItemRenderer i = Minecraft.getInstance().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(GrenadeEntity.class, manager -> new SpriteRenderer<>(manager, i));
    }

    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(MetadataTile.class, new MetadataTESR());
    }

    @Override
    public void openMetaTile(MetadataTile tile) {
        Minecraft.getInstance().displayGuiScreen(new MetadataScreen(tile));
    }
}
