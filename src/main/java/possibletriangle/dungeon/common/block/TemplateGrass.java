package possibletriangle.dungeon.common.block;

import net.minecraft.block.*;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.registries.ObjectHolder;
import possibletriangle.dungeon.DungeonMod;

import java.util.Random;

@ObjectHolder(DungeonMod.MODID)
public class TemplateGrass extends TemplateBlock {

    public TemplateGrass(Type type) {
        super(type);
    }

    @Override
    public boolean canSustainPlant(BlockState p_canSustainPlant_1_, IBlockReader p_canSustainPlant_2_, BlockPos p_canSustainPlant_3_, Direction p_canSustainPlant_4_, IPlantable p_canSustainPlant_5_) {
        return true;
    }

}
