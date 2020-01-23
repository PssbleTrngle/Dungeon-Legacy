package possibletriangle.dungeon.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.EditStructureScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.resources.I18n;
import net.minecraft.state.properties.StructureMode;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import possibletriangle.dungeon.common.block.tile.MetadataTile;

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
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        this.nameEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 40, 300, 20, I18n.format("structure_block.structure_name")) {
            public boolean charTyped(char character, int index) {
                return MetadataScreen.this.isValidCharacterForName(this.getText(), character, this.getCursorPosition()) && super.charTyped(character, index);
            }
        };
        this.nameEdit.setMaxStringLength(64);
        this.nameEdit.setText(tile.getName());
        this.children.add(this.nameEdit);

        this.categoriesEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 80, 300, 20, I18n.format("metadata_block.categories")) {};
        this.categoriesEdit.setMaxStringLength(128);
        this.categoriesEdit.setText(tile.getName());
        this.children.add(this.categoriesEdit);

        this.weightEdit = new TextFieldWidget(this.font, this.width / 2 - 152, 100, 80, 20, I18n.format("metadata_block.weight"));
        this.weightEdit.setMaxStringLength(6);
        this.weightEdit.setText(Float.toString(tile.getMeta().getWeight()));
        this.children.add(this.weightEdit);

        this.setFocusedDefault(this.nameEdit);

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
