package possibletriangle.dungeon;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import possibletriangle.dungeon.block.tile.TileEntitySpawn;
import possibletriangle.dungeon.block.tile.TileFakeWall;
import possibletriangle.dungeon.block.tile.tesr.TESRFakeWall;
import possibletriangle.dungeon.block.tile.tesr.TESRSpawn;
import possibletriangle.dungeon.entity.EntityCurse;
import possibletriangle.dungeon.entity.render.RenderEntityCurse;

public class ClientProxy extends CommonProxy {

    @Override
    public void preinit(FMLPreInitializationEvent event) {
        super.preinit(event);

        RenderingRegistry.registerEntityRenderingHandler(EntityCurse.class, RenderEntityCurse::new);
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Dungeon.MODID, id),"inventory"));
    }

    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySpawn.class, new TESRSpawn());
        ClientRegistry.bindTileEntitySpecialRenderer(TileFakeWall.class, new TESRFakeWall());
    }
}
