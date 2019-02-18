package possibletriangle.dungeon.generator;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import possibletriangle.dungeon.Dungeon;

import java.io.IOException;

public class GuiDungeon extends GuiScreen implements GuiSlider.FormatHelper, GuiPageButtonList.GuiResponder {

    private final GuiScreen parent;
    private final DungeonOptions options;
    private GuiButton rotate, ceiling;

    public GuiDungeon(GuiScreen parent, DungeonOptions options) {
        this.parent = parent;
        this.options = options;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, "Dungeon Options", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {

        this.buttonList.clear();

        addButton(new GuiSlider(this, 10, this.width / 2 - 75, 50, "Floors", 1, DungeonOptions.MAX_FLOORS, options.floorCount(), this));

        addButton(rotate = new GuiButton(1, this.width / 2 - 55 + 60, 80, 110, 20, ""));
        addButton(ceiling = new GuiButton(2, this.width / 2 - 55 - 60, 80, 110, 20, ""));

        addButton(new GuiButton(20, this.width / 2 - 55 + 60, 200, 110, 20, I18n.format("gui.done")));
        addButton(new GuiButton(21, this.width / 2 - 55 - 60, 200, 110, 20, I18n.format("gui.cancel")));

        updateStates();
    }

    private void updateStates() {

        rotate.displayString = "Rotate " + I18n.format(options.rotateRooms() ? "options.on" : "options.off");
        ceiling.displayString = "Ceiling " + I18n.format(options.hasCeiling() ? "options.on" : "options.off");

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        switch (button.id) {
            case 20:
                options.apply();
                this.mc.displayGuiScreen(this.parent);
                break;
            case 21:
                options.cancel();
                this.mc.displayGuiScreen(this.parent);
                break;
            case 1:
                options.setRotate(!options.rotateRooms());
                updateStates();
                break;
            case 2:
                options.setCeiling(!options.hasCeiling());
                updateStates();
                break;
        }

    }

    @Override
    public void setEntryValue(int id, boolean value) {
    }

    @Override
    public void setEntryValue(int id, float value) {
        if(id == 10) options.setFloorCount((int) value);
    }

    @Override
    public void setEntryValue(int id, String value) {
    }

    @Override
    public String getText(int id, String name, float value) {
        return (int) value + " floors";
    }
}
