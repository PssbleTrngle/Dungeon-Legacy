package possibletriangle.dungeon.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import possibletriangle.dungeon.common.block.tile.MetadataTile;
import possibletriangle.dungeon.common.world.structure.metadata.StructureMetadata;

import java.util.Arrays;

public class MetadataScreen extends Screen {

    private TextFieldWidget nameEdit, weightEdit, categoriesEdit;
    private final MetadataTile tile;

    public MetadataScreen(MetadataTile tile) {
        super(new StringTextComponent("Structure Metadata"));
        this.tile = tile;
    }

    @Override
    public void tick() {
        this.nameEdit.tick();
        this.weightEdit.tick();
        this.categoriesEdit.tick();
    }

    @Override
    protected void init() {
        StructureMetadata meta = tile.getMeta();

        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.nameEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 40, 300, 20, I18n.format("structure_block.structure_name")) {
            public boolean charTyped(char character, int index) {
                return MetadataScreen.this.isValidCharacterForName(this.getText(), character, this.getCursorPosition()) && super.charTyped(character, index);
            }
        };
        this.nameEdit.setMaxStringLength(64);
        this.nameEdit.setText(tile.getName());
        this.children.add(this.nameEdit);

        this.categoriesEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 70, 300, 20, I18n.format("metadata_block.categories")) {};
        this.categoriesEdit.setMaxStringLength(128);
        this.categoriesEdit.setText(String.join(", ", meta.getCategories()));
        this.children.add(this.categoriesEdit);

        this.weightEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 100, 80, 20, I18n.format("metadata_block.weight"));
        this.weightEdit.setMaxStringLength(6);
        this.weightEdit.setText(Float.toString(meta.getWeight()));
        this.children.add(this.weightEdit);

        this.setFocusedDefault(this.nameEdit);

    }

    /**
     * Generates a new StructureMetadata object from the given input
     * Used to store in TileEntity when pressing OK and
     * saved to File when pressing SAVE
     */
    public StructureMetadata generateMeta() {

        String name = this.nameEdit.getText();

        String[] categories = Arrays.stream(this.categoriesEdit.getText()
            .split(","))
            .map(String::trim)
            .distinct()
            .filter(s -> s.matches("^[a-zA-Z0-9-_]+$"))
            .toArray(String[]::new);

        float weight = Float.parseFloat(weightEdit.getText());

        StructureMetadata original = tile.getMeta();
        return new StructureMetadata(weight, name, original.getConditions(), categories, original.getParts());
    }

    public void removed() {
        this.minecraft.keyboardListener.enableRepeatEvents(false);
    }

    public void render(int width, int height, float f) {
        this.renderBackground();

        String name = "structure_block.mode_info." + "save";
        this.drawString(this.font, I18n.format(name), this.width / 2 - 153, 174, 10526880);

        this.drawString(this.font, I18n.format("structure_block.structure_name"), this.width / 2 - 153, 30, 10526880);
        this.nameEdit.render(width, height, f);

        this.weightEdit.render(width, height, f);

        this.categoriesEdit.render(width, height, f);

        super.render(width, height, f);
    }

    public boolean isPauseScreen() {
        return false;
    }

}