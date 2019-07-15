package com.NovumScientiaTeam.modulartoolkit.tables.screens;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.ModificationStationContainer;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.ModificationStationTile;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ModificationStationScreen extends ContainerScreen<ModificationStationContainer> {
    protected ModificationStationTile te;
    public static final ResourceLocation background = new ResourceLocation(ModularToolkit.MOD_ID + ":textures/gui/modification_station.png");
    private PlayerInventory playerInventory;
    private int lastX = 0;
    private int lastY = 0;
    private byte mode = 0;

    public ModificationStationScreen(ModificationStationContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.te = container.getTe();
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(ModificationStationScreen.background);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = title.getFormattedText();
        this.font.drawString(name, (float) (this.xSize / 2 - this.font.getStringWidth(name) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        return super.mouseClicked(mouseX, mouseY, clickType);
    }
}
