package com.NovumScientiaTeam.modulartoolkit.tables.blocks;

import com.EmosewaPixel.pixellib.blocks.ModBlock;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class ToolConstructorBlock extends ModBlock {
    public ToolConstructorBlock() {
        super(Block.Properties.from(Blocks.CRAFTING_TABLE), ModularToolkit.MOD_ID + ":tool_constructor", 0);
    }
}
