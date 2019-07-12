package com.NovumScientiaTeam.modulartoolkit.recipes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntArrayNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ConstructorPattern {
    public int[][] pattern;

    public ConstructorPattern() {
        this(new int[0][0]);
    }

    public ConstructorPattern(int[][] pattern) {
        this.pattern = pattern;
    }

    public boolean patternMatches(ConstructorPattern pattern2) {
        ConstructorPattern constructorPattern1 = this.copy().cleanPattern();
        ConstructorPattern constructorPattern2 = pattern2.copy().cleanPattern();
        if (constructorPattern1.pattern.length != constructorPattern2.pattern.length || constructorPattern1.pattern[0].length != constructorPattern2.pattern[0].length)
            return false;
        for (int y = 0; y < constructorPattern1.pattern.length; y++) {
            for (int x = 0; x < constructorPattern1.pattern[0].length; x++) {
                if (constructorPattern1.pattern[y][x] != constructorPattern2.pattern[y][x])
                    return false;
            }
        }
        return true;
    }

    public ConstructorPattern cleanPattern() {
        if (pattern.length > 0 && pattern[0].length > 0) {
            List<Integer> colsToRemove = new ArrayList<>();
            List<Integer> rowsToRemove = new ArrayList<>();
            int firstX = pattern[0].length - 1, lastX = 0;
            int firstY = pattern.length - 1, lastY = 0;
            for (int x = 0; x < pattern[0].length; x++) {
                for (int y = 0; y < pattern.length; y++) {
                    if (pattern[y][x] == 1) {
                        if (x < firstX) firstX = x;
                        if (x > lastX) lastX = x;
                        if (y < firstY) firstY = y;
                        if (y > lastY) lastY = y;
                    }
                }
            }
            for (int x = 0; x < pattern[0].length; x++) {
                if (x < firstX || x > lastX) colsToRemove.add(x);
            }
            for (int y = 0; y < pattern.length; y++) {
                if (y < firstY || y > lastY) rowsToRemove.add(y);
            }
            removeCols(colsToRemove);
            removeRows(rowsToRemove);
        }
        return this;
    }

    private void removeRows(List<Integer> rowsToRemove) {
        int[][] newPattern = new int[pattern.length - rowsToRemove.size()][pattern[0].length];
        int curNewY = 0;
        for (int y = 0; y < pattern.length; y++) {
            if (!rowsToRemove.contains(y)) {
                for (int x = 0; x < pattern[y].length; x++) {
                    newPattern[curNewY][x] = pattern[y][x];
                }
                curNewY++;
            }
        }
        pattern = newPattern;
    }

    private void removeCols(List<Integer> colsToRemove) {
        int[][] newPattern = new int[pattern.length][pattern[0].length - colsToRemove.size()];
        int curNewX = 0;
        for (int x = 0; x < pattern[0].length; x++) {
            if (!colsToRemove.contains(x)) {
                for (int y = 0; y < pattern.length; y++) {
                    newPattern[y][curNewX] = pattern[y][x];
                }
                curNewX++;
            }
        }
        pattern = newPattern;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();

        int height = pattern.length;
        int width = pattern[0].length;

        ListNBT list = new ListNBT();
        for (int i = 0; i < height; i++) {
            list.add(new IntArrayNBT(pattern[i]));
        }

        compound.put("array", list);
        compound.putInt("height", height);
        compound.putInt("width", width);
        return compound;
    }

    public void deserializeNBT(CompoundNBT compound) {
        int height = Math.max(1, compound.getInt("height"));
        int width = Math.max(1, compound.getInt("width"));
        pattern = new int[height][width];
        ListNBT list = compound.getList("array", Constants.NBT.TAG_INT_ARRAY);
        for (int i = 0; i < height; i++) {
            pattern[i] = list.getIntArray(i);
        }
    }

    public ConstructorPattern copy() {
        int[][] array = new int[pattern.length][pattern[0].length];
        for (int y = 0; y < pattern.length; y++) {
            for (int x = 0; x < pattern[0].length; x++) {
                array[y][x] = pattern[y][x];
            }
        }
        return new ConstructorPattern(array);
    }
}