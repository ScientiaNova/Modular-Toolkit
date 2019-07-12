package com.NovumScientiaTeam.modulartoolkit.tables.screens;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.PacketHandler;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.PacketPattern;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.PartConstructorContainer;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.PartConstructorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

public class PartConstructorScreen extends ContainerScreen<PartConstructorContainer> {
    protected PartConstructorTile te;
    private String background = ModularToolkit.MOD_ID + ":textures/gui/part_constructor.png";
    private PlayerInventory playerInventory;

    public PartConstructorScreen(PartConstructorContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.te = container.getTe();
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation(this.background));
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        for (int y = 0; y < te.getCurrentPattern().pattern.length; y++) {
            for (int x = 0; x < te.getCurrentPattern().pattern[0].length; x++) {
                this.blit(this.guiLeft + 48 + 5 * x, this.guiTop + 26 + y * 5, te.getCurrentPattern().pattern[y][x] == 1 ? 181 : 176, 1, 5, 5);
            }
        }
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = title.getFormattedText();
        this.font.drawString(name, (float) (this.xSize / 2 - this.font.getStringWidth(name) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {

        if (mouseX >= this.guiLeft + 48 && mouseX < this.guiLeft + 48 + 5 * 7 && mouseY >= this.guiTop + 26 && mouseY < this.guiTop + 26 + 5 * 7) {
            int x = (int) Math.floor(mouseX - this.guiLeft - 48) / 5;
            int y = (int) Math.floor(mouseY - this.guiTop - 26) / 5;
            te.getCurrentPattern().pattern[y][x] = te.getCurrentPattern().pattern[y][x] == 1 ? 0 : 1;
            PacketHandler.INSTANCE.sendToServer(new PacketPattern(te.getPos(), te.getCurrentPattern()));
            Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        } else
            return super.mouseClicked(mouseX, mouseY, clickType);
    }
}
