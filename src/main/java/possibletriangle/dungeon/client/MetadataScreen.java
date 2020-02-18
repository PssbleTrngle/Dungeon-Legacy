package possibletriangle.dungeon.client;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.StringTextComponent;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.world.structure.metadata.Part;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;
import possibletriangle.dungeon.common.world.structure.metadata.condition.CategoryCondition;
import possibletriangle.dungeon.helper.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.function.Supplier;

public class MetadataScreen extends Screen {

    private final MetadataTile tile;

    private int partCount = 0;

    /**
     *  The list of functions to execute on saving, supplying different parts of the StructureMetadata NBT
     */
    private final List<Pair<String,Supplier<INBT>>> suppliers = Lists.newArrayList();

    public MetadataScreen(MetadataTile tile) {
        super(new StringTextComponent("Structure Metadata"));
        this.tile = tile;
    }

    @Override
    public void tick() {
        children(TextFieldWidget.class).forEach(TextFieldWidget::tick);
    }

    @Override
    protected void init() {
        StructureMetadata meta = tile.getMeta().clone();
        
        /* Just copy the conditions for now */
        this.suppliers.add(new Pair<>("conditions", () -> meta.serializeNBT().get("conditions")));

        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.addButton(new Button(this.width / 2 - 4 - 150, 210, 150, 20, I18n.format("metadata_block.add_part"), $ -> addPart()));

        this.addButton(new Button(this.width / 2 + 4 + 100, 185, 50, 20, I18n.format("structure_block.button.save"), $ -> {
            CompoundNBT m = generateMeta().serializeNBT();
        }));

        TextFieldWidget nameEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 40, 300, 20, I18n.format("structure_block.structure_name")) {
            public boolean charTyped(char character, int index) {
                return MetadataScreen.this.isValidCharacterForName(this.getText(), character, this.getCursorPosition()) && super.charTyped(character, index);
            }
        };
        nameEdit.setMaxStringLength(64);
        nameEdit.setText(tile.getName());
        this.children.add(nameEdit);
        this.suppliers.add(new Pair<>("display", () -> new StringNBT(nameEdit.getText())));

        TextFieldWidget categoriesEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 70, 300, 20, I18n.format("metadata_block.categories")) {};
        categoriesEdit.setMaxStringLength(128);
        categoriesEdit.setText(String.join(", ", meta.getCategories()));
        this.children.add(categoriesEdit);
        this.suppliers.add(new Pair<>("categories", () -> StructureMetadata.serializeList(
            Arrays.stream(categoriesEdit.getText()
                .split(","))
                .map(String::trim)
                .distinct()
                .filter(s -> s.matches("^[a-zA-Z0-9-_]+$"))
        )));

        TextFieldWidget weightEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 100, 80, 20, I18n.format("metadata_block.weight"));
        weightEdit.setMaxStringLength(6);
        weightEdit.setText(Float.toString(meta.getWeight()));
        this.children.add(weightEdit);
        this.suppliers.add(new Pair<>("weight", () -> new FloatNBT(getFloat(weightEdit))));

        Arrays.stream(meta.getParts()).forEach(this::addPart);

        this.setFocusedDefault(nameEdit);

    }

    private int getInt(TextFieldWidget edit) {
        try {
            return Integer.parseInt(edit.getText());
        } catch(NumberFormatException ex) {
            return 0;
        }
    }

    private float getFloat(TextFieldWidget edit) {
        try {
            return Float.parseFloat(edit.getText());
        } catch(NumberFormatException ex) {
            return 0;
        }
    }

    private void addPart() {
        addPart(null);
    }

    private void addPart(@Nullable Part in) {
        final int index = partCount;

        Map<String, Function<AxisAlignedBB,Double>> c = new HashMap<String, Function<AxisAlignedBB,Double>>() {{
            put("X", b -> b.minX);
            put("Y", b -> b.minY);
            put("Z", b -> b.minZ);
        }};

        List<TextFieldWidget> posEdits = Lists.newArrayList();
        c.forEach((key, val) -> {
            TextFieldWidget edit = new TextFieldWidget(this.font, width / 2 - 152 +  posEdits.size() * 25, 140 + 30 * index, 20, 20, I18n.format("structure_block.pos" + key));
            edit.setMaxStringLength(6);
            edit.setText(Optional.ofNullable(in).map(p -> val.apply(p.getPos())).orElse(0D).toString());
            posEdits.add(edit);
        });

        children.addAll(posEdits);

        Pair<String,Supplier<INBT>> supplier = new Pair<>("parts", () -> {
            CategoryCondition categories = new CategoryCondition(new String[0], new String[0], new String[0]);
            AxisAlignedBB size = new AxisAlignedBB(3, 1, 3, 3, 7, 3);

            List<Integer> pos = posEdits.stream().map(this::getInt).collect(Collectors.toList());
            Vec3d p = new Vec3d(pos.get(0), pos.get(1), pos.get(2));
            Part part = new Part(categories, new AxisAlignedBB(p, p), size);

            ListNBT list = new ListNBT();
            list.add(part.serializeNBT());
            return list;
        });
        this.suppliers.add(supplier);

        addButton(new Button(width / 2 - 72, 140 + 30 * index, 20, 20, I18n.format("metadata_block.remove_part"), button -> {
            posEdits.forEach(this.children::remove);
            this.children.remove(button);
            this.suppliers.remove(supplier);
            partCount--;
        }));

        partCount++;
    }

    /**
     * Generates a new StructureMetadata object from the given input
     * Used to store in TileEntity when pressing OK and
     * saved to File when pressing SAVE
     * @return The generated Metadata
     */
    public StructureMetadata generateMeta() {

        /* TODO check if nbt merging will replace lists or merge them */
        CompoundNBT merged = this.suppliers.stream().map(pair -> {
            INBT nbt = pair.getSecond().get();
            CompoundNBT compound = new CompoundNBT();
            if(nbt != null) compound.put(pair.getFirst(), nbt);
            return compound;
        }).reduce(new CompoundNBT(), CompoundNBT::merge, (a, b) -> a);

        StructureMetadata meta = StructureMetadata.getDefault();
        meta.deserializeNBT(merged);
        return meta;
    }

    public void removed() {
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    public void render(int width, int height, float f) {
        this.renderBackground();

        //String name = "structure_block.mode_info." + "save";
        //this.drawString(this.font, I18n.format(name), this.width / 2 - 153, 174, 10526880);

        this.drawString(this.font, I18n.format("structure_block.structure_name"), this.width / 2 - 153, 30, 10526880);

        children(Widget.class).forEach(w -> w.render(width, height, f));

        super.render(width, height, f);
    }

    public <T> Collection<T> children(Class<T> clazz) {
        return this.children()
                .stream()
                .filter(clazz::isInstance)
                .map(g -> (T) g)
                .collect(Collectors.toList());
    }

    public boolean isPauseScreen() {
        return false;
    }

}
