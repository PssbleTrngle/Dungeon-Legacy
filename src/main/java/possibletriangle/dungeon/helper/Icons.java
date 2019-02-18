package possibletriangle.dungeon.helper;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import possibletriangle.dungeon.Dungeon;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class Icons {

    public static TextureAtlasSprite VORTEX, VORTEX_GLOBAL, BARRIER, WALL;

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre evt) {

        BARRIER = evt.getMap().registerSprite(new ResourceLocation("minecraft:items/barrier"));
        VORTEX = evt.getMap().registerSprite(new ResourceLocation("minecraft:blocks/dragon_egg"));
        VORTEX_GLOBAL = evt.getMap().registerSprite(new ResourceLocation("minecraft:blocks/emerald_block"));
        WALL = evt.getMap().registerSprite(new ResourceLocation("dungeon:blocks/placeholder_wall"));

    }

}