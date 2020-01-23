package possibletriangle.dungeon.common.world.structure.metadata.condition;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import possibletriangle.dungeon.common.world.GenerationContext;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Condition<K> implements INBTSerializable<CompoundNBT>, Predicate<K> {

    private String[] allow;
    private String[] reject;
    private String[] required;

    protected Condition(String[] allow, String[] reject, String[] required) {
        this.allow = allow;
        this.reject = reject;
        this.required = required;
    }

    protected abstract BiPredicate<String,K> getPredicate();

    private Optional<Predicate<K>> mergeForArray(String[] array, boolean negate) {
        if(array.length == 0) return Optional.empty();

        return Optional.of(Arrays.stream(array)
                    .map(i -> (Predicate<K>) ctx -> getPredicate().test(i, ctx) != negate)
                    .reduce(ctx -> false, Predicate::or, (p1, p2) -> p1));
    }

    public boolean test(K k) {

        Predicate<K> p1 = mergeForArray(reject, false).orElse(ctx -> false).negate();
        Predicate<K> p2 = mergeForArray(allow, false).orElse(ctx -> true);
        Predicate<K> p3 = mergeForArray(required, true).orElse(ctx -> false).negate();

        return p1.and(p2).and(p3).test(k);
    }

    private String[] fromList(ListNBT list) {
        return (String[]) list.stream().map(INBT::toString).toArray();
    }

    private ListNBT toList(String[] t) {
        ListNBT list = new ListNBT();
        Arrays.stream(t)
                .map(Object::toString)
                .map(StringNBT::new)
                .forEach(list::add);
        return list;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();

        nbt.put("allow", toList(allow));
        nbt.put("reject", toList(allow));
        nbt.put("required", toList(required));

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(nbt.contains("allow")) this.allow = fromList(nbt.getList("allow", 8));
        if(nbt.contains("reject")) this.allow = fromList(nbt.getList("reject", 8));
        if(nbt.contains("required")) this.allow = fromList(nbt.getList("required", 8));
    }
}
