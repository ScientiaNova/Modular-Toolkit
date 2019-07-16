package com.NovumScientiaTeam.modulartoolkit.tables.screens;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.packets.PacketHandler;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.PartConstructorContainer;
import com.NovumScientiaTeam.modulartoolkit.packets.PatternPacket;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.PartConstructorTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;

public class PartConstructorScreen extends ContainerScreen<PartConstructorContainer> {
    protected PartConstructorTile te;
    public static final ResourceLocation background = new ResourceLocation(ModularToolkit.MOD_ID + ":textures/gui/part_constructor.png");
    private PlayerInventory playerInventory;
    private int lastX = 0;
    private int lastY = 0;
    private byte mode = 0;

    public PartConstructorScreen(PartConstructorContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.te = container.getTe();
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(PartConstructorScreen.background);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int mx = getXFromMouse(mouseX);
        int my = getYFromMouse(mouseY);

        for (int y = 0; y < te.getCurrentPattern().pattern.length; y++)
            for (int x = 0; x < te.getCurrentPattern().pattern[0].length; x++)
                if (te.getCurrentPattern().pattern[y][x] == 1)
                    this.blit(this.guiLeft + 54 + 6 * x, this.guiTop + 22 + y * 6, 182, 1, 6, 6);
                else if (isMouseInPattern(mouseX, mouseY) && mx == x && my == y)
                    this.blit(this.guiLeft + 54 + 6 * x, this.guiTop + 22 + y * 6, 176, 1, 6, 6);
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

        if (isMouseOnReset(mouseX, mouseY))
            renderTooltip(new TranslationTextComponent("modulartoolkit.button.tooltip.reset").getString(), mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        if (clickType == GLFW.GLFW_MOUSE_BUTTON_LEFT)
            if (isMouseInPattern(mouseX, mouseY)) {
                int x = getXFromMouse(mouseX);
                int y = getYFromMouse(mouseY);
                te.getCurrentPattern().pattern[y][x] = mode = (byte) (te.getCurrentPattern().pattern[y][x] == 1 ? 0 : 1);
                PacketHandler.INSTANCE.sendToServer(new PatternPacket(te.getPos(), te.getCurrentPattern()));
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                lastX = x;
                lastY = y;
                return true;
            } else if (isMouseOnReset(mouseX, mouseY)) {
                te.getCurrentPattern().pattern = new byte[7][7];
                PacketHandler.INSTANCE.sendToServer(new PatternPacket(te.getPos(), te.getCurrentPattern()));
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
        return super.mouseClicked(mouseX, mouseY, clickType);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int clickType, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        if (clickType == GLFW.GLFW_MOUSE_BUTTON_LEFT && isMouseInPattern(mouseX, mouseY)) {
            int x = getXFromMouse(mouseX);
            int y = getYFromMouse(mouseY);
            if ((x != lastX || y != lastY) && te.getCurrentPattern().pattern[y][x] != mode) {
                te.getCurrentPattern().pattern[y][x] = mode;
                PacketHandler.INSTANCE.sendToServer(new PatternPacket(te.getPos(), te.getCurrentPattern()));
                Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            }
            lastX = x;
            lastY = y;
        }
        return super.mouseDragged(mouseX, mouseY, clickType, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    private int getXFromMouse(double mouseX) {
        return (int) Math.floor(mouseX - this.guiLeft - 54) / 6;
    }

    private int getYFromMouse(double mouseY) {
        return (int) Math.floor(mouseY - this.guiTop - 22) / 6;
    }

    private boolean isMouseInPattern(double mouseX, double mouseY) {
        return mouseX >= this.guiLeft + 54 && mouseX < this.guiLeft + 96 && mouseY >= this.guiTop + 22 && mouseY < this.guiTop + 64;
    }

    private boolean isMouseOnReset(double mouseX, double mouseY) {
        return mouseX >= this.guiLeft + 69 && mouseX <= this.guiLeft + 81 && mouseY >= this.guiTop + 68 && mouseY <= this.guiTop + 78;
    }
}
