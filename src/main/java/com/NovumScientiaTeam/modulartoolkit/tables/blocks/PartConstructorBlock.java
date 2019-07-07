package com.NovumScientiaTeam.modulartoolkit.tables.blocks;

import com.EmosewaPixel.pixellib.blocks.ModBlock;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import net.minecraft.block.Blocks;

public class PartConstructorBlock extends ModBlock {
    public PartConstructorBlock() {
        super(Properties.from(Blocks.CRAFTING_TABLE), ModularToolkit.MOD_ID + ":part_constructor", 0);
    }
}
