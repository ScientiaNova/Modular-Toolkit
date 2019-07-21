package com.NovumScientiaTeam.modulartoolkit.tables.screens;

import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import com.NovumScientiaTeam.modulartoolkit.items.ModularItem;
import com.NovumScientiaTeam.modulartoolkit.items.util.ModularUtils;
import com.NovumScientiaTeam.modulartoolkit.packets.BoostsPacket;
import com.NovumScientiaTeam.modulartoolkit.packets.PacketHandler;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.ModificationStationContainer;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.ModificationStationTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;

public class ModificationStationScreen extends ContainerScreen<ModificationStationContainer> {
    protected ModificationStationTile te;
    public static final ResourceLocation background = new ResourceLocation(ModularToolkit.MOD_ID, "textures/gui/modification_station.png");
    private PlayerInventory playerInventory;

    public ModificationStationScreen(ModificationStationContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.playerInventory = playerInventory;
        this.te = container.getTe();
        this.xSize = 448;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(ModificationStationScreen.background);
        this.blit(this.guiLeft, this.guiTop, 0, 0, 176, this.ySize);

        if (hasModifiers())
            if (isMouseOnBoost(mouseX, mouseY))
                this.blit(guiLeft + 103, guiTop + 47, 183, 0, 7, 7);
            else
                this.blit(guiLeft + 103, guiTop + 47, 176, 0, 7, 7);

        this.minecraft.getTextureManager().bindTexture(new ResourceLocation(ModularToolkit.MOD_ID, "textures/gui/stats_panel.png"));
        this.blit(this.guiLeft - 136, this.guiTop, 0, 0, 136, this.ySize);
        this.blit(this.guiLeft + 176, this.guiTop, 0, 0, 136, this.ySize);
    }

    @Override
    protected void init() {
        super.init();
        this.guiLeft += 136;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = title.getFormattedText();
        font.drawString(name, 88 - this.font.getStringWidth(name) / 2f, 6.0F, 4210752);
        font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, this.ySize - 94, 4210752);

        ItemStack input = container.getSlot(0).getStack();
        if (!input.isEmpty()) {
            List<ITextComponent> inputStats = new ArrayList<>();
            ((ModularItem) input.getItem()).addStats(input, inputStats, true);
            for (int i = 0; i < inputStats.size(); i++)
                font.drawString(inputStats.get(i).getFormattedText(), -128, 8 + i * 10, 0xffffff);

            ItemStack output = container.getSlot(5).getStack();
            if (!output.isEmpty()) {
                List<ITextComponent> outputputStats = new ArrayList<>();
                ((ModularItem) output.getItem()).addStats(output, outputputStats, true);
                for (int i = 0; i < outputputStats.size(); i++)
                    font.drawString(outputputStats.get(i).getFormattedText(), 184, 8 + i * 10, 0xffffff);
            }
        }
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
        return !ModularUtils.isBroken(output) && ModularUtils.getFreeModifierSlotCount(output) > 0;
    }
}