package possibletriangle.dungeon.common.world.structure;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import io.netty.util.internal.StringUtil;
import javafx.util.Pair;
import net.minecraft.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.world.PistonEvent;
import org.lwjgl.system.CallbackI;
import possibletriangle.dungeon.common.world.GenerationContext;

import javax.sound.midi.MetaEventListener;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;

public class StructureMetadata {

    public static final Serializer SERIALIZER = new Serializer();

    public final float weight;
    public final Predicate<GenerationContext> predicate;

    public StructureMetadata(float weight, Predicate<GenerationContext> predicate) {
        this.weight = weight;
        this.predicate = predicate;
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

            List<Predicate<GenerationContext>> predicates = Lists.newArrayList();
            conditions.forEach(c -> {
                JsonObject condition = c.getAsJsonObject();
                if(JSONUtils.hasField(condition, "type")) {

                    String type = JSONUtils.getString(condition, "type");
                    JsonArray blacklist = JSONUtils.getJsonArray(condition, "reject", new JsonArray());
                    JsonArray whitelist = JSONUtils.getJsonArray(condition, "allow", new JsonArray());

                    Predicate<GenerationContext> predicate = predicateFor(type, blacklist, whitelist);
                    if(predicate != null) predicates.add(predicate);

                }
            });

            Predicate<GenerationContext> predicate = predicates.stream().reduce(ctx -> true, Predicate::and, (p1, p2) -> p1);
            return new StructureMetadata(weight, predicate);
        }

        private <T> Predicate<GenerationContext> predicateForOne(JsonArray list, BiPredicate<T,GenerationContext> single, Function<JsonElement, T> parse) {
            return StreamSupport.stream(Spliterators.spliteratorUnknownSize(list.iterator(), Spliterator.ORDERED), false)
                        .map(parse)
                        .map(i -> (Predicate<GenerationContext>) ctx -> single.test(i, ctx))
                        .reduce(ctx -> false, Predicate::or, (p1, p2) -> p1);
        }

        private <T> Predicate<GenerationContext> predicateForAll(JsonArray blacklist, JsonArray whitelist, BiPredicate<T,GenerationContext> single, Function<JsonElement, T> parse) {
            if(whitelist.size() > 0)
                return predicateForOne(whitelist, single, parse);
            else if(blacklist.size() > 0)
                return predicateForOne(blacklist, single, parse).negate();

            return ctx -> true;
        }

        private Predicate<GenerationContext> predicateFor(String type, JsonArray blacklist, JsonArray whitelist) {
            switch (type) {

                case "floor":
                    return predicateForAll(blacklist, whitelist, (floor, ctx) -> {
                        if(floor >= 0) return ctx.floor == floor;
                        int floors = ctx.settings.floors;
                        return ctx.floor - floors == floor;
                    }, JsonElement::getAsInt);

                default:
                    return null;
            }
        }

    }

}
