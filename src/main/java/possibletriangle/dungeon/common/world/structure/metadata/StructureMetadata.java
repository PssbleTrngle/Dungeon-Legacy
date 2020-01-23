package possibletriangle.dungeon.common.world.structure.metadata;

import com.google.gson.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraftforge.common.util.INBTSerializable;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.structure.metadata.condition.Condition;
import possibletriangle.dungeon.common.world.structure.metadata.condition.GenerationCondition;

import java.util.*;
import java.util.function.Predicate;

public class StructureMetadata implements INBTSerializable<CompoundNBT> {

    public static final Serializer SERIALIZER = new Serializer();

    private String display;
    private float weight;
    private GenerationCondition[] conditions;
    private String[] categories;
    private Part[] parts;

    public String getDisplay() {
        return display;
    }

    public float getWeight() {
        return weight;
    }

    public Predicate<GenerationContext> getPredicate() {
        return Arrays.stream(conditions)
            .reduce(ctx -> true, Predicate::and, (p1, p2) -> p1);
    }

    public String[] getCategories() {
        return categories;
    }

    public Part[] getParts() {
        return parts;
    }

    public StructureMetadata(float weight, String display, GenerationCondition[] conditions, String[] categories, Part[] parts) {
        this.weight = weight;
        this.conditions = conditions;
        this.display = display;
        this.categories = categories;
        this.parts = parts;
    }

    public StructureMetadata(float weight, String display, String... categories) {
        this(weight, display, new GenerationCondition[0], categories, new Part[0]);
    }

    public static StructureMetadata getDefault() {
        return SERIALIZER.deserialize(new JsonObject());
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putFloat("weight", weight);
        nbt.putString("display", display);
        ListNBT categories = new ListNBT();

        Arrays.stream(this.categories).map(StringNBT::new).forEach(categories::add);
        nbt.put("categories", categories);

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(nbt.contains("weight")) this.weight = nbt.getFloat("weight");
        if(nbt.contains("display")) this.display = nbt.getString("display");
        if(nbt.contains("categories")) this.categories = nbt.getList("categories", 8).stream().map(INBT::toString).toArray(String[]::new);
    }

}
