package com.NovumScientiaTeam.modulartoolkit.tables.screens;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.packets.BoostsPacket;
import com.NovumScientiaTeam.modulartoolkit.packets.PacketHandler;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.ModificationStationContainer;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.ModificationStationTile;
import com.NovumScientiaTeam.modulartoolkit.tools.util.ToolUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ModificationStationScreen extends ContainerScreen<ModificationStationContainer> {
    protected ModificationStationTile te;
    public static final ResourceLocation background = new ResourceLocation(ModularToolkit.MOD_ID + ":textures/gui/modification_station.png");
    private PlayerInventory playerInventory;

    public ModificationStationScreen(ModificationStationContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.te = container.getTe();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(ModificationStationScreen.background);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        if (hasModifiers())
            if (isMouseOnBoost(mouseX, mouseY))
                this.blit(guiLeft + 103, guiTop + 47, 183, 0, 7, 7);
            else
                this.blit(guiLeft + 103, guiTop + 47, 176, 0, 7, 7);
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

        if (isMouseOnBoost(mouseX, mouseY))
            renderTooltip(new TranslationTextComponent("modulartoolkit.button.tooltip.boost").getString(), mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickType) {
        if (isMouseOnBoost(mouseX, mouseY) && hasModifiers()) {
            te.setBoosts(te.getBoosts() + 1);
            PacketHandler.INSTANCE.sendToServer(new BoostsPacket(te.getPos(), te.getBoosts()));
            Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        }

        return super.mouseClicked(mouseX, mouseY, clickType);
    }

    private boolean isMouseOnBoost(double mouseX, double mouseY) {
        return mouseX >= guiLeft + 103 && mouseX <= guiLeft + 110 && mouseY >= guiTop + 47 && mouseY <= guiTop + 54;
    }

    public boolean hasModifiers() {
        ItemStack output = container.getSlot(5).getStack();
        if (output.isEmpty())
            return false;
        return !ToolUtils.isBroken(output) && ToolUtils.getFreeModifierSlotCount(output) > 0;
    }
}