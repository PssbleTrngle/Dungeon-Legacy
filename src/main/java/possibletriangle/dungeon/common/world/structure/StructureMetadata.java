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
    private Predicate<GenerationContext> predicate;
    private String[] categories;
    private Part[] parts;

    public String getDisplay() {
        return display;
    }

    public float getWeight() {
        return weight;
    }

    public Predicate<GenerationContext> getPredicate() {
        return predicate;
    }

    public String[] getCategories() {
        return categories;
    }

    public Part[] getParts() {
        return parts;
    }

    public static class Part implements Predicate<Generateable> {

        private final Predicate<String[]> categories;
        private final AxisAlignedBB pos;
        private final AxisAlignedBB size;

        public Part(Predicate<String[]> categories, AxisAlignedBB pos, AxisAlignedBB size) {
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

    public StructureMetadata(float weight, String display, Predicate<GenerationContext> predicate, String[] categories, Part[] parts) {
        this.weight = weight;
        this.predicate = predicate;
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
            JsonArray conditions = JSONUtils.getJsonArray(json, "conditions", new JsonArray());
            String[] categories = arrayToStream(JSONUtils.getJsonArray(json, "categories", new JsonArray()), JsonElement::getAsString).toArray(String[]::new);

            String display = JSONUtils.getString(json, "name", "???");

            List<Predicate<GenerationContext>> predicates = Lists.newArrayList();
            conditions.forEach(c -> {
                JsonObject condition = c.getAsJsonObject();
                if(JSONUtils.hasField(condition, "type")) {

                    String type = JSONUtils.getString(condition, "type");
                    JsonArray blacklist = JSONUtils.getJsonArray(condition, "reject", new JsonArray());
                    JsonArray whitelist = JSONUtils.getJsonArray(condition, "allow", new JsonArray());

                    Predicate<GenerationContext> predicate = getPredicate(type, blacklist, whitelist);
                    if(predicate != null) predicates.add(predicate);

                }
            });

            /* Merge predicates using AND */
            Predicate<GenerationContext> predicate = predicates.stream().reduce(ctx -> true, Predicate::and, (p1, p2) -> p1);

            Part[] parts = getParts(json);

            return new StructureMetadata(weight, display, predicate, categories, parts);
        }

        <T> Stream<T> arrayToStream(JsonArray array, Function<JsonElement, T> parse) {
            return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(array.iterator(), Spliterator.ORDERED), false)
                .map(parse);
        }

        /**
         * Pulls all predicates from a JsonArray
         * @param list The JsonArray containing the JsonElements
         * @param parse Function to parse a JsonElement to the required type (ex: JsonElement::getAsInt)
         * @param single Predicate to test a single entry against the K
         * @return A merged predicated using OR
         */
        private <T,K> Predicate<K> predicateForOne(JsonArray list, Function<JsonElement, T> parse, BiPredicate<T,K> single) {
            return arrayToStream(list, parse)
                        .map(i -> (Predicate<K>) ctx -> single.test(i, ctx))
                        .reduce(ctx -> false, Predicate::or, (p1, p2) -> p1);
        }

        /**
         * Pull predicates from either the whitelist.
         * If the provided whitelist is empty, use the blacklist
         * @param parse Function to parse a JsonElement to the required type (ex: JsonElement::getAsInt)
         * @param single Predicate to test a single entry against the K
         * @return A merged predicate using OR
         */
        private <T,K> Predicate<K> predicateForAll(JsonArray blacklist, JsonArray whitelist, Function<JsonElement, T> parse, BiPredicate<T,K> single) {
            if(whitelist.size() > 0)
                return predicateForOne(whitelist, parse, single);
            else if(blacklist.size() > 0)
                return predicateForOne(blacklist, parse, single).negate();

            return ctx -> true;
        }

        /**
         * @param type The type of condition
         * @return the predicate
         */
        private Predicate<GenerationContext> getPredicate(String type, JsonArray blacklist, JsonArray whitelist) {

            switch (type) {

                case "floor":
                    return predicateForAll(blacklist, whitelist, JsonElement::getAsInt, (Integer floor, GenerationContext ctx) -> {
                        if(floor >= 0) return ctx.floor == floor;
                        int floors = ctx.settings.floors;
                        return ctx.floor - floors == floor;
                    });

                case "mod":
                    return predicateForAll(blacklist, whitelist, JsonElement::getAsString,
                            (String modid, GenerationContext ctx)-> ModList.get().isLoaded(modid));

                default:
                    return ctx -> true;
            }
        }

        private Pair<Integer, Integer> getCoordinate(JsonElement element) {
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

        private AxisAlignedBB getPos(JsonObject json) {

            Pair<Integer, Integer> x = getCoordinate(json.get("x"));
            Pair<Integer, Integer> y = getCoordinate(json.get("y"));
            Pair<Integer, Integer> z = getCoordinate(json.get("z"));

            return new AxisAlignedBB(x.getKey(), y.getKey(), z.getKey(), x.getValue(), y.getValue(), z.getValue());

        }

        private Part[] getParts(JsonObject json) {

            List<Part> list = Lists.newArrayList();
            JsonArray parts = JSONUtils.getJsonArray(json, "parts", new JsonArray());

            arrayToStream(parts, JsonElement::getAsJsonObject).forEach(part -> {
                
                JsonArray categories = JSONUtils.getJsonArray(part, "categories", new JsonArray());
                Predicate<String[]> predicate = arrayToStream(categories, JsonElement::getAsJsonObject)
                    .map(condition -> {

                        JsonArray blacklist = JSONUtils.getJsonArray(condition, "reject", new JsonArray());
                        JsonArray whitelist = JSONUtils.getJsonArray(condition, "allow", new JsonArray());
                        return predicateForAll(blacklist, whitelist, JsonElement::getAsString, (String cat, String[] given) -> Arrays.asList(given).contains(cat));

                    })
                    .reduce(c -> true, Predicate::and, (p1, p2) -> p1);

                try {
                    AxisAlignedBB pos = getPos(JSONUtils.getJsonObject(part, "pos"));
                    AxisAlignedBB size = getPos(JSONUtils.getJsonObject(part, "size"));

                    list.add(new Part(predicate, pos, size));
                } catch(JsonSyntaxException ex) {
                    DungeonMod.LOGGER.error("A structure is missing position or size for a substructure part, part will be skipped");
                }
            });

            return list.toArray(new Part[0]);

        }

    }

}
