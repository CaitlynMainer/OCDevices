package ben_mkiv.ocdevices.client.gui;

import ben_mkiv.ocdevices.OCDevices;
import ben_mkiv.ocdevices.common.inventory.CardDockContainer;
import ben_mkiv.ocdevices.common.tileentity.TileEntityCardDock;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class CardDockGUI extends GuiContainer {
    public static final int WIDTH = 175;
    public static final int HEIGHT = 195;

    TileEntityCardDock te;

    private static final ResourceLocation background = new ResourceLocation(OCDevices.MOD_ID, "textures/gui/carddock.png");

    public CardDockGUI(TileEntityCardDock tileEntity, CardDockContainer container) {
        super(container);
        te = tileEntity;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    void drawCenteredString(String string, int y, int color){
        FontRenderer fr = mc.fontRenderer;
        fr.drawString(string, getXSize()/2 - fr.getStringWidth(string)/2, y, color);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        drawCenteredString("Card Dock", 5, 4210752);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}

