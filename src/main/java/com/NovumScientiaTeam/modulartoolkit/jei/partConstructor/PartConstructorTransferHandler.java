package com.NovumScientiaTeam.modulartoolkit.jei.partConstructor;

import com.EmosewaPixel.pixellib.PixelLib;
import com.EmosewaPixel.pixellib.materialsystem.lists.MaterialItems;
import com.EmosewaPixel.pixellib.materialsystem.materials.IMaterialItem;
import com.NovumScientiaTeam.modulartoolkit.PacketHandler;
import com.NovumScientiaTeam.modulartoolkit.jei.partConstructor.recipetransfer.AdvancedRecipeTransferPacket;
import com.NovumScientiaTeam.modulartoolkit.jei.partConstructor.recipetransfer.AdvancedRecipeTransferUtils;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPattern;
import com.NovumScientiaTeam.modulartoolkit.recipes.ConstructorPatternRegistry;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.PartConstructorContainer;
import com.NovumScientiaTeam.modulartoolkit.tables.containers.PatternPacket;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.config.ServerInfo;
import mezz.jei.transfer.BasicRecipeTransferInfo;
import mezz.jei.util.Translator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PartConstructorTransferHandler implements IRecipeTransferHandler<PartConstructorContainer> {
    private final IRecipeTransferHandlerHelper handlerHelper;
    private final IRecipeTransferInfo<PartConstructorContainer> transferHelper;

    public PartConstructorTransferHandler(IRecipeTransferHandlerHelper handlerHelper) {
        this.handlerHelper = handlerHelper;
        this.transferHelper = new BasicRecipeTransferInfo<>(PartConstructorContainer.class, PartConstructorCategory.ID, 0, 1, 3, 36);
    }

    @Override
    public Class<PartConstructorContainer> getContainerClass() {
        return PartConstructorContainer.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(PartConstructorContainer container, IRecipeLayout recipeLayout, PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
        if (!ServerInfo.isJeiOnServer()) {
            String tooltipMessage = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.no.server");
            return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
        }

        Map<Integer, Slot> inventorySlots = new HashMap<>();
        for (Slot slot : transferHelper.getInventorySlots(container))
            inventorySlots.put(slot.slotNumber, slot);

        Slot inputSlot = container.getSlot(0);
        Map<Integer, Slot> craftingSlots = new HashMap<>();
        craftingSlots.put(container.getSlot(0).slotNumber, container.getSlot(0));

        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();

        Map<Integer, ItemStack> availableItemStacks = new HashMap<>();

        if (!inputSlot.getStack().isEmpty()) {
            if (!inputSlot.canTakeStack(player)) {
                PixelLib.LOGGER.error("Recipe Transfer helper {} does not work for container {}. Player can't move item out of Crafting Slot number 0", transferHelper.getClass(), container.getClass());
                return handlerHelper.createInternalError();
            }
            availableItemStacks.put(inputSlot.slotNumber, inputSlot.getStack().copy());
        }

        for (Slot slot : inventorySlots.values()) {
            final ItemStack stack = slot.getStack();
            if (!stack.isEmpty())
                availableItemStacks.put(slot.slotNumber, stack.copy());
        }

        int[] matchingSlots = AdvancedRecipeTransferUtils.getMatchingItems(availableItemStacks, itemStackGroup.getGuiIngredients().get(0).getAllIngredients());

        ConstructorPattern pattern;
        Item output = itemStackGroup.getGuiIngredients().get(1).getDisplayedIngredient().getItem();
        if (output instanceof IMaterialItem)
            pattern = ConstructorPatternRegistry.getPattern(((IMaterialItem) output).getObjType());
        else
            pattern = ConstructorPatternRegistry.getPattern(MaterialItems.getItemObjType(output));

        pattern = new ConstructorPattern(PartRecipe.centerPatern(pattern.pattern));

        if (matchingSlots.length == 0) {
            String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.missing");
            return handlerHelper.createUserErrorForSlots(message, Collections.singleton(0));
        }

        // check that the slots exist and can be altered
        for (int i : matchingSlots) {
            if (i < 0 || i >= container.inventorySlots.size()) {
                PixelLib.LOGGER.error("Recipes Transfer Helper {} references slot {} outside of the inventory's size {}", transferHelper.getClass(), i, container.inventorySlots.size());
                return handlerHelper.createInternalError();
            }
        }

        if (doTransfer) {
            PacketHandler.INSTANCE.sendToServer(new AdvancedRecipeTransferPacket(matchingSlots, itemStackGroup.getGuiIngredients().get(0).getDisplayedIngredient().getCount(), maxTransfer));
            PacketHandler.INSTANCE.sendToServer(new PatternPacket(container.getTe().getPos(), pattern));
        }

        return null;
    }
}