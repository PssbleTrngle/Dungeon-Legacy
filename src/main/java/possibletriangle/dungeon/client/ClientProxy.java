package possibletriangle.dungeon.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import possibletriangle.dungeon.common.CommonProxy;
import possibletriangle.dungeon.common.block.tile.MetadataTile;

public class ClientProxy extends CommonProxy {

    @Override
    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegistry.bindTileEntitySpecialRenderer(MetadataTile.class, new MetadataTESR());
    }
}
