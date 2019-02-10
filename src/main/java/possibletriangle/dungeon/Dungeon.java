package possibletriangle.dungeon;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;
import possibletriangle.dungeon.command.CommandDungeon;

@Mod.EventBusSubscriber
@Mod(modid = Dungeon.MODID, name = Dungeon.NAME, version = Dungeon.VERSION)
public class Dungeon
{
    public static final String MODID = "dungeon";
    public static final String NAME = "Dungeon";
    public static final String VERSION = "Alpha";

    public static Logger LOGGER;

    @SidedProxy(serverSide = "possibletriangle.dungeon.CommonProxy", clientSide = "possibletriangle.dungeon.ClientProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();

        proxy.preinit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postinit(event);
    }

    @EventHandler
    public static void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandDungeon());
    }

}
