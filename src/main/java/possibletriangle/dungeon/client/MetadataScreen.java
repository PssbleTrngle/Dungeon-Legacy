package possibletriangle.dungeon.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;
import possibletriangle.dungeon.helper.Pair;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.nbt.INBT;

import java.util.Arrays;

public class MetadataScreen extends Screen {

    private TextFieldWidget nameEdit, weightEdit, categoriesEdit;
    private final MetadataTile tile;

    /**
     * @return A list of runnables removing the specific text fields and supplier for a part
     */
    private List<Runnable> parts = List.newArrayList();

    /**
     * @return The list of functions to execute on saving, supplying different parts of the StructureMetadata NBT
     */
    private final List<Pair<String,Supplier<INBT>>> suppliers = Lists.newArrayList();

    public MetadataScreen(MetadataTile tile) {
        super(new StringTextComponent("Structure Metadata"));
        this.tile = tile;
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
        this.weightEdit.tick();
        this.categoriesEdit.tick();
        /* TODO Tick all children */
    }

    @Override
    protected void init() {
        StructureMetadata meta = tile.getMeta().copy();
        
        /* Just copy the conditions for now */
        this.suppliers.add(new Pair<>("conditions", () -> meta.serializeNBT().get("conditions")));

        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.nameEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 40, 300, 20, I18n.format("structure_block.structure_name")) {
            public boolean charTyped(char character, int index) {
                return MetadataScreen.this.isValidCharacterForName(this.getText(), character, this.getCursorPosition()) && super.charTyped(character, index);
            }
        };
        this.nameEdit.setMaxStringLength(64);
        this.nameEdit.setText(tile.getName());
        this.children.add(this.nameEdit);
        this.suppliers.add(new Pair<>("name", () -> new StringNBT(this.nameEdit.getText())));

        this.categoriesEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 60, 300, 20, I18n.format("metadata_block.categories")) {};
        this.categoriesEdit.setMaxStringLength(128);
        this.categoriesEdit.setText(String.join(", ", meta.getCategories()));
        this.children.add(this.categoriesEdit);
        this.suppliers.paddut(new Pair<>("categories", () -> StructureMetadata.serializeList(
            Arrays.stream(this.categoriesEdit.getText()
                .split(","))
                .map(String::trim)
                .distinct()
                .filter(s -> s.matches("^[a-zA-Z0-9-_]+$"))
        )));

        this.weightEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 100, 80, 20, I18n.format("metadata_block.weight"));
        this.weightEdit.setMaxStringLength(6);
        this.weightEdit.setText(Float.toString(meta.getWeight()));
        this.children.add(this.weightEdit);

        this.setFocusedDefault(this.nameEdit);

    }

    private void removePart(int index) {
        Optional.ofNullable(parts.get(index)).ifPresent(remove -> {
            remove.run();
            parts.remove(index);
        });
    }

    private int getNumber(TextFieldWidget edit) {
        try {
            return Integer.parseInt(edit.getText());
        } catch(NumberFormatException ex) {
            return 0;
        }
    }

    private void addPart() {
        final int index = parts.length;
        
        List<TextFieldWidget> posEdits = Stream.of("X", "Y", "Z").map(c -> {
            TextFieldWidget edit = new TextFieldWidget(this.font, this.width / 2 - 152, 140 + 40 * index, 300, 20, I18n.format("structure_block.pos" + c)) {};
            edit.setMaxStringLength(6);
            edit.setText("");
            this.children.add(edit);
            return edit;
        }).collect(Collectors.toList());

        Pair<String,Supplier<INBT>> supplier = new Pair<>("parts", () -> {
            CategoryCondition categories = new CategoryCondition(new String[0], new String[0], new String[0]);
            AxisAlignedBB size = new AxisAlignedBB(3, 1, 3, 3, 7, 3);

            List<Integer> pos = posEdits.stream().map(this::getNumber).collect(Collectors.toList());
            Vec3d p = new Vec3d(pos.get(0), pos.get(1), pos.get(2));
            Part part = new Part(categories, new AxisAlignedBB(p, p), size);
            
            ListNBT list = new ListNBT();
            list.add(part.serializeNBT());
            return list;
        });
        this.suppliers.add(supplier);

        parts.add(() -> {
            posEdits.stream().forEach(this.children::remove);
            this.suppliers.remove(supplier);
        });
        
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
            CompoundNBT nbt = pair.getSecond().get();
            CompoundNBT compound = new CompoundNBT();
            compound.set(pair.getFirst(), nbt);
            return compound;
        }).reduce(new CompoundNBT(), (c, s) -> c.merge(s), (a, b) -> a);

        StructureMetadata meta = StructureMetadata.getDefault();
        meta.deserualizeNBT(merged);
        return meta;
    }

    public void removed() {
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    public void render(int width, int height, float f) {
        this.renderBackground();

        String name = "structure_block.mode_info." + "save";
        this.drawString(this.font, I18n.format(name), this.width / 2 - 153, 174, 10526880);

        //this.drawString(this.font, I18n.format("structure_block.structure_name"), this.width / 2 - 153, 30, 10526880);
        this.nameEdit.render(width, height, f);

        this.weightEdit.render(width, height, f);

        this.categoriesEdit.render(width, height, f);

        super.render(width, height, f);
    }

    public boolean isPauseScreen() {
        return false;
    }

}
