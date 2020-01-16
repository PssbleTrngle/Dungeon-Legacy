package possibletriangle.dungeon.common.world.structure;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import possibletriangle.dungeon.common.world.GenerationContext;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class StructureMetadata {

    public static final Serializer SERIALIZER = new Serializer();

    public final String display;
    public final float weight;
    public final Predicate<GenerationContext> predicate;

    public StructureMetadata(float weight, String display, Predicate<GenerationContext> predicate) {
        this.weight = weight;
        this.predicate = predicate;
        this.display = display;
    }

    public StructureMetadata(float weight, String display) {
        this(weight, display, ctx -> true);
    }

    /**
     * @return The default Metadata, used if a structure file does not define its own
     */
    public static StructureMetadata getDefault() {
        return SERIALIZER.deserialize(new JsonObject());
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
            return new StructureMetadata(weight, display, predicate);
        }

        /**
         * Pulls all predicates from a JsonArray
         * @param list The JsonArray containing the JsonElements
         * @param parse Function to parse a JsonElement to the required type (ex: JsonElement::getAsInt)
         * @param single Predicate to test a single entry against the GenerationContext
         * @return A merged predicated using OR
         */
        private <T> Predicate<GenerationContext> predicateForOne(JsonArray list, Function<JsonElement, T> parse, BiPredicate<T,GenerationContext> single) {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(list.iterator(), Spliterator.ORDERED), false)
                        .map(parse)
                        .map(i -> (Predicate<GenerationContext>) ctx -> single.test(i, ctx))
                        .reduce(ctx -> false, Predicate::or, (p1, p2) -> p1);
        }

        /**
         * Pull predicates from either the whitelist.
         * If the provided whitelist is empty, use the blacklist
         * @param parse Function to parse a JsonElement to the required type (ex: JsonElement::getAsInt)
         * @param single Predicate to test a single entry against the GenerationContext
         * @return A merged predicate using OR
         */
        private <T> Predicate<GenerationContext> predicateForAll(JsonArray blacklist, JsonArray whitelist, Function<JsonElement, T> parse, BiPredicate<T,GenerationContext> single) {
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
                    return predicateForAll(blacklist, whitelist, JsonElement::getAsInt, (floor, ctx) -> {
                        if(floor >= 0) return ctx.floor == floor;
                        int floors = ctx.settings.floors;
                        return ctx.floor - floors == floor;
                    });

                case "mod":
                    return predicateForAll(blacklist, whitelist, JsonElement::getAsString, (modid, ctx) -> ModList.get().isLoaded(modid));

                default:
                    return null;
            }
        }

    }

}
