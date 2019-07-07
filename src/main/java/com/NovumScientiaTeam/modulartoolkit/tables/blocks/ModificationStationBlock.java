package com.NovumScientiaTeam.modulartoolkit.tables.blocks;

import com.EmosewaPixel.pixellib.blocks.ModBlock;
import com.NovumScientiaTeam.modulartoolkit.ModularToolkit;
import net.minecraft.block.Blocks;

public class ModificationStationBlock extends ModBlock {
    public ModificationStationBlock() {
        super(Properties.from(Blocks.CRAFTING_TABLE), ModularToolkit.MOD_ID + ":modification_station", 0);
    }
}