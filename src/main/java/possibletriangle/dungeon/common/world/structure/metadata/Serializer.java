package possibletriangle.dungeon.common.world.structure.metadata;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javafx.util.Pair;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import possibletriangle.dungeon.DungeonMod;
import possibletriangle.dungeon.common.world.structure.metadata.condition.CategoryCondition;
import possibletriangle.dungeon.common.world.structure.metadata.condition.Condition;
import possibletriangle.dungeon.common.world.structure.metadata.condition.GenerationCondition;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Serializer implements IMetadataSectionSerializer<StructureMetadata> {

    @Override
    public String getSectionName() {
        return "structure";
    }

    @Override
    public StructureMetadata deserialize(JsonObject json) {

        float weight = JSONUtils.getFloat(json, "weight", 1F);
        JsonArray conditionJson = JSONUtils.getJsonArray(json, "conditions", new JsonArray());
        String[] categories = arrayToStream(JSONUtils.getJsonArray(json, "categories", new JsonArray()), JsonElement::getAsString).toArray(String[]::new);

        String display = JSONUtils.getString(json, "name", "???");

        GenerationCondition[] conditions = arrayToStream(conditionJson, JsonElement::getAsJsonObject)
            .map(Serializer::getGenerationGeneration)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toArray(GenerationCondition[]::new);

        Part[] parts = getParts(json);

        return new StructureMetadata(weight, display, conditions, categories, parts);
    }

    static <T> Stream<T> arrayToStream(JsonArray array, Function<JsonElement, T> parse) {
        return StreamSupport
            .stream(Spliterators.spliteratorUnknownSize(array.iterator(), Spliterator.ORDERED), false)
            .map(parse);
    }

    /**
     * @param object The JSON object
     * @return The parsed condition if valid
     */
    private static Optional<GenerationCondition> getGenerationGeneration(JsonObject object) {

        String type = JSONUtils.getString(object, "type", "");

        String[] allow = getStringArray(object, "allow");
        String[] reject = getStringArray(object, "reject");
        String[] required = getStringArray(object, "required");

        switch (type) {

            case "floor":
                return Optional.of(new GenerationCondition(allow, reject, required, new ResourceLocation(DungeonMod.MODID, "floor")));

            case "mod":
                return Optional.of(new GenerationCondition(allow, reject, required, new ResourceLocation(DungeonMod.MODID, "mod")));

            case "palette":
                return Optional.of(new GenerationCondition(allow, reject, required, new ResourceLocation(DungeonMod.MODID, "palette")));

            default:
                return Optional.empty();
        }
    }

    /**
     * Retrieve a coordinate either in the format
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

    private static String[] getStringArray(JsonObject object, String key) {
        JsonArray array = JSONUtils.getJsonArray(object, key, new JsonArray());
        return arrayToStream(array, JsonElement::toString).toArray(String[]::new);
    }

    private static Part[] getParts(JsonObject json) {

        List<Part> list = Lists.newArrayList();
        JsonArray parts = JSONUtils.getJsonArray(json, "parts", new JsonArray());

        arrayToStream(parts, JsonElement::getAsJsonObject).forEach(part -> {

            JsonObject c = JSONUtils.getJsonObject(part, "categories", new JsonObject());

            String[] allow = getStringArray(c, "allow");
            String[] reject = getStringArray(c, "reject");
            String[] required = getStringArray(c, "required");

            Condition<String[]> condition = new CategoryCondition(allow, reject, required);

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
