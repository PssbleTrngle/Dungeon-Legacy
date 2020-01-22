package possibletriangle.dungeon.common.world.structure;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.gen.feature.template.Template;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.DungeonChunk;
import possibletriangle.dungeon.common.world.DungeonSettings;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.room.Generateable;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DungeonStructure extends Generateable {

    private final StructureMetadata meta;
    private BlockPos size;
    private final List<List<Template.BlockInfo>> blocks = Lists.newArrayList();
    private final List<Template.EntityInfo> entities = Lists.newArrayList();

    public DungeonStructure(StructureMetadata meta) {
        this.meta = meta;
    }

    public StructureMetadata getMeta() {
        return this.meta;
    }

    @Override
    public Vec3i getActualSize() {
        return new Vec3i(16, this.size.getY(), 16);
    }

    @Override
    public void generate(DungeonChunk chunk, Random random, GenerationContext context, BlockPos at) {

        this.blocks.forEach(l -> l.forEach(block -> {
            BlockPos p = at.add(block.pos);
            chunk.setBlockState(p, block.state);
            if(block.nbt != null) chunk.setTileEntity(p, block.nbt);
        }));

    }

    public void read(CompoundNBT nbt) {
        this.blocks.clear();
        this.entities.clear();

        ListNBT size = nbt.getList("size", 3);
        this.size = new BlockPos(size.getInt(0), size.getInt(1), size.getInt(2));

        ListNBT blocks = nbt.getList("blocks", 10);
        ListNBT palettes;
        if (nbt.contains("palettes", 9)) {
            palettes = nbt.getList("palettes", 9);

            for(int i = 0; i < palettes.size(); ++i) {
                this.doSomething(palettes.getList(i), blocks);
            }
        } else {
            this.doSomething(nbt.getList("palette", 10), blocks);
        }

        palettes = nbt.getList("entities", 10);

        for(int i = 0; i < palettes.size(); ++i) {
            CompoundNBT palette = palettes.getCompound(i);
            ListNBT lvt_7_1_ = palette.getList("pos", 6);
            Vec3d lvt_8_1_ = new Vec3d(lvt_7_1_.getDouble(0), lvt_7_1_.getDouble(1), lvt_7_1_.getDouble(2));
            ListNBT lvt_9_1_ = palette.getList("blockPos", 3);
            BlockPos lvt_10_1_ = new BlockPos(lvt_9_1_.getInt(0), lvt_9_1_.getInt(1), lvt_9_1_.getInt(2));
            if (palette.contains("nbt")) {
                CompoundNBT lvt_11_1_ = palette.getCompound("nbt");
                this.entities.add(new Template.EntityInfo(lvt_8_1_, lvt_10_1_, lvt_11_1_));
            }
        }

    }

    private void doSomething(ListNBT p_204768_1_, ListNBT p_204768_2_) {
        BasicPalette lvt_3_1_ = new BasicPalette();
        List<Template.BlockInfo> lvt_4_1_ = Lists.newArrayList();

        int lvt_5_2_;
        for(lvt_5_2_ = 0; lvt_5_2_ < p_204768_1_.size(); ++lvt_5_2_) {
            lvt_3_1_.addMapping(NBTUtil.readBlockState(p_204768_1_.getCompound(lvt_5_2_)), lvt_5_2_);
        }

        for(lvt_5_2_ = 0; lvt_5_2_ < p_204768_2_.size(); ++lvt_5_2_) {
            CompoundNBT lvt_6_1_ = p_204768_2_.getCompound(lvt_5_2_);
            ListNBT lvt_7_1_ = lvt_6_1_.getList("pos", 3);
            BlockPos lvt_8_1_ = new BlockPos(lvt_7_1_.getInt(0), lvt_7_1_.getInt(1), lvt_7_1_.getInt(2));
            BlockState lvt_9_1_ = lvt_3_1_.stateFor(lvt_6_1_.getInt("state"));
            CompoundNBT lvt_10_2_;
            if (lvt_6_1_.contains("nbt")) {
                lvt_10_2_ = lvt_6_1_.getCompound("nbt");
            } else {
                lvt_10_2_ = null;
            }

            lvt_4_1_.add(new Template.BlockInfo(lvt_8_1_, lvt_9_1_, lvt_10_2_));
        }

        lvt_4_1_.sort(Comparator.comparingInt((p_215384_0_) -> {
            return p_215384_0_.pos.getY();
        }));
        this.blocks.add(lvt_4_1_);
    }

    private ListNBT writeInts(int... p_186267_1_) {
        ListNBT lvt_2_1_ = new ListNBT();
        int[] var3 = p_186267_1_;
        int var4 = p_186267_1_.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int lvt_6_1_ = var3[var5];
            lvt_2_1_.add(new IntNBT(lvt_6_1_));
        }

        return lvt_2_1_;
    }

    static class BasicPalette implements Iterable<BlockState> {
        public static final BlockState DEFAULT_BLOCK_STATE;
        private final ObjectIntIdentityMap<BlockState> ids;
        private int lastId;

        private BasicPalette() {
            this.ids = new ObjectIntIdentityMap(16);
        }

        public int idFor(BlockState p_189954_1_) {
            int lvt_2_1_ = this.ids.get(p_189954_1_);
            if (lvt_2_1_ == -1) {
                lvt_2_1_ = this.lastId++;
                this.ids.put(p_189954_1_, lvt_2_1_);
            }

            return lvt_2_1_;
        }

        @Nullable
        public BlockState stateFor(int p_189955_1_) {
            BlockState lvt_2_1_ = (BlockState)this.ids.getByValue(p_189955_1_);
            return lvt_2_1_ == null ? DEFAULT_BLOCK_STATE : lvt_2_1_;
        }

        public Iterator<BlockState> iterator() {
            return this.ids.iterator();
        }

        public void addMapping(BlockState p_189956_1_, int p_189956_2_) {
            this.ids.put(p_189956_1_, p_189956_2_);
        }

        static {
            DEFAULT_BLOCK_STATE = Blocks.AIR.getDefaultState();
        }
    }

}
