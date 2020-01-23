package possibletriangle.dungeon.common.world.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import javafx.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.GenerationContext;
import possibletriangle.dungeon.common.world.room.Generateable;
import possibletriangle.dungeon.common.world.room.StructureType;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StructureMetadata implements INBTSerializable<CompoundNBT> {

    public static final Serializer SERIALIZER = new Serializer();

    private String display;
    private float weight;
    private Condition<?,GenerationContext>[] conditions;
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

    public static class Part implements Predicate<Generateable> {

        private final Condition<String,String[]> categories;
        private final AxisAlignedBB pos;
        private final AxisAlignedBB size;

        public Part(Condition<String,String[]> categories, AxisAlignedBB pos, AxisAlignedBB size) {
            this.size = size;
            this.pos = pos;
            this.categories = categories;
        }

        public boolean test(Generateable structure) {
            Vec3i size = structure.getActualSize();
            return categories.test(structure.getMeta().categories)
                && size.getX() >= this.size.minX && size.getX() <= this.size.maxX
                && size.getY() >= this.size.minY && size.getY() <= this.size.maxY
                && size.getZ() >= this.size.minZ && size.getZ() <= this.size.maxZ;
        }

    }

    public static class Condition<T,K> implements Predicate<K>, INBTSerializable {

        public static final BiPredicate<Integer, GenerationContext> FLOOR = (floor, ctx) -> {
            if(floor >= 0) return ctx.floor == floor;
            int floors = ctx.settings.floors;
            return ctx.floor - floors == floor;
        });

        public static final BiPredicate<String, GenerationContext> MOD = (modid, ctx) ->
            ModList.get().isLoaded(modid)));

        public static final BiPredicate<ResourceLocation, GenerationContext> FLOOR = (palette, ctx) ->
            ctx.palette.getResourceName().equals(palette)));


        private T[] allow;
        private T[] reject;
        private T[] required;
        private final BiPredicate<T,K> single;

        public Condition(T[] allow, T[] reject, T[] required, BiPredicate<T,K> single) {
            this.allow = allow;
            this.reject = reject;
            this.required = required;
            this.single = single;
        }

        private Optional<Predicate<K>> mergeForArray(T[] array) {
            if(list.size() == 0) return Optional.empty();
            
            return Optional.of(Arrays.stream(array)
                        .map(i -> (Predicate<K>) ctx -> single.test(i, ctx))
                        .reduce(ctx -> false, Predicate::or, (p1, p2) -> p1));
        }

        public boolean test(K k) {

            Predicate<K> p1 = mergeForArray(reject).orElse(ctx -> false).negate();
            Predicate<K> p2 = mergeForArray(allow).orElse(ctx -> true);
            Predicate<K> p3 = mergeForArray(required.negate()).orElse(ctx -> false).negate();

            return p1.and(p2).and(p3).test(k);
        }

        private T[] fromList(ListNBT list) {
            return (T[]) list.stream().map(INBT::toString).toArray();
        }

        private toList(T[] t) {
            ListNBT list = new ListNBT();
            list.addAll(t);
            return list;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = new CompoundNBT();
    
            nbt.putList("allow", toList(allow));
            nbt.putList("reject", toList(allow));
            nbt.putList("required", toList(required));
    
            return nbt;
        }
    
        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            if(nbt.contains("allow")) this.allow = fromList(nbt.getList("allow", 8));
            if(nbt.contains("reject")) this.allow = fromList(nbt.getList("reject", 8));
            if(nbt.contains("required")) this.allow = fromList(nbt.getList("required", 8));
        }

    }

    public StructureMetadata(float weight, String display, Condition<GeneractionContext,?> conditions, String[] categories, Part[] parts) {
        this.weight = weight;
        this.conditions = conditions;
        this.display = display;
        this.categories = categories;
        this.parts = parts;
    }

    public StructureMetadata(float weight, String display, String... categories) {
        this(weight, display, ctx -> true, categories, new Part[0]);
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

    public static class Serializer implements IMetadataSectionSerializer<StructureMetadata> {

        @Override
        public String getSectionName() {
            return "structure";
        }

        @Override
        public StructureMetadata deserialize(JsonObject json) {

            float weight = JSONUtils.getFloat(json, "weight", 1F);
            JsonArray condtionJson = JSONUtils.getJsonArray(json, "conditions", new JsonArray());
            String[] categories = arrayToStream(JSONUtils.getJsonArray(json, "categories", new JsonArray()), JsonElement::getAsString).toArray(String[]::new);

            String display = JSONUtils.getString(json, "name", "???");

            Condtion<GenerationContext,?>[] conditions = arrayToStream(condtionJson, JsonElement::getAsJsonObject)
                .map(Serializer::getGenerationCondition)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray();

            Part[] parts = getParts(json);

            return new StructureMetadata(weight, display, conditions, categories, parts);
        }

        <T> Stream<T> arrayToStream(JsonArray array, Function<JsonElement, T> parse) {
            return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(array.iterator(), Spliterator.ORDERED), false)
                .map(parse);
        }

        private static <T> T[] parseJsonArray(JsonArray in, Function<JsonElement,T> parse) {
            return (T[]) arrayToStream(in, parse).toArray();
        }

        private static <T,K> Condition<T,K> parseCondition(JsonArray allow, JsonArray reject, JsonArray required, BiPredicate<T,K> predicate, Function<JsonElement,T> parse) {
            return new Condition(
                parseJsonArray(allow, parse),
                parseJsonArray(reject, parse),
                parseJsonArray(required, parse),
                predicate
            );
        }

        /**
         * @param object The JSON object
         * @return The parsed condition if valid
         */
        private static Optional<Condition<?,GenerationContext>> getGenerationCondition(JsonObject object) {

            String type = JSONUtils.getString(c, "type", null);

            JsonArray allow = JSONUtils.getJsonArray(object, "allow", new JsonArray());
            JsonArray reject = JSONUtils.getJsonArray(object, "reject", new JsonArray());
            JsonArray required = JSONUtils.getJsonArray(object, "required", new JsonArray());

            switch (type) {

                case "floor":
                    return Optional.of(parseCondition(allow, reject, required, Condition.FLOOR, JsonElement::getAsInt));

                case "mod":
                    return Optional.of(parseCondition(allow, reject, required, Condition.MOD, JsonElement::getAsString));

                case "palette":
                    return Optional.of(parseCondition(allow, reject, required, Condition.PALETTE, e -> new ResourceLocation(e.getAsString())));

                default:
                    return Optional.empty();
            }
        }

        /**
         * Retrive a coordinate either in the format 
         *        x: 0
         * or
         *        x: { from: 0, to: 2 }  
         * 
         * @param element The JSON Element to read from
         * @return A pair representing the min and max values
         */
        private static Pair<Integer, Integer> getCoordinate(JsonElement element) {
            if(JSONUtils.isNumber(element)) {

                int i = element.getAsInt();
                return new Pair<>(i, i);

            } else if(element.isJsonObject()) {

                int from = JSONUtils.getInt(element, "from");
                int to = JSONUtils.getInt(element, "to");

                return new Pair<>(from, to);

            }

            return new Pair<>(0, 0);
        }

        private static AxisAlignedBB getPos(JsonObject json) {

            Pair<Integer, Integer> x = getCoordinate(json.get("x"));
            Pair<Integer, Integer> y = getCoordinate(json.get("y"));
            Pair<Integer, Integer> z = getCoordinate(json.get("z"));

            return new AxisAlignedBB(x.getKey(), y.getKey(), z.getKey(), x.getValue(), y.getValue(), z.getValue());

        }

        private static Part[] getParts(JsonObject json) {

            List<Part> list = Lists.newArrayList();
            JsonArray parts = JSONUtils.getJsonArray(json, "parts", new JsonArray());

            arrayToStream(parts, JsonElement::getAsJsonObject).forEach(part -> {
                
                JsonObject c = JSONUtils.JsonObject(part, "categories", new JsonObject());
                
                JsonArray allow = JSONUtils.getJsonArray(c, "allow", new JsonArray());
                JsonArray reject = JSONUtils.getJsonArray(c, "reject", new JsonArray());
                JsonArray required = JSONUtils.getJsonArray(c, "required", new JsonArray());

                Condtion<String,String[]> condition = parseCondition(allow, reject, required, 
                (String cat, String[] given) -> Arrays.asList(given).contains(cat), JsonElement::getAsString);


                try {
                    AxisAlignedBB pos = getPos(JSONUtils.getJsonObject(part, "pos"));
                    AxisAlignedBB size = getPos(JSONUtils.getJsonObject(part, "size"));

                    list.add(new Part(condition, pos, size));
                } catch(JsonSyntaxException ex) {
                    DungeonMod.LOGGER.error("A structure is missing position or size for a substructure part, part will be skipped");
                }
            });

            return list.toArray(new Part[0]);

        }

    }

}
