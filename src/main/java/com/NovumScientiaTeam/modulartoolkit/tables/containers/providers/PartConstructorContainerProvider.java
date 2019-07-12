package com.NovumScientiaTeam.modulartoolkit.tables.containers.providers;

import com.NovumScientiaTeam.modulartoolkit.tables.containers.PartConstructorContainer;
import com.NovumScientiaTeam.modulartoolkit.tables.tiles.PartConstructorTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class PartConstructorContainerProvider implements INamedContainerProvider {
    protected BlockPos pos;
    private ResourceLocation name;

    public PartConstructorContainerProvider(BlockPos pos, ResourceLocation name) {
        this.pos = pos;
        this.name = name;
    }

    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new PartConstructorContainer(id, playerInventory, (PartConstructorTile) playerEntity.world.getTileEntity(this.pos));
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block." + this.name.getNamespace() + "." + this.name.getPath(), new Object[0]);
    }
}
