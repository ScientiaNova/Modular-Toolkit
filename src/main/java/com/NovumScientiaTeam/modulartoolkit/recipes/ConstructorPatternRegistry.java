package com.NovumScientiaTeam.modulartoolkit.recipes;

import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.NovumScientiaTeam.modulartoolkit.ObjTypeRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConstructorPatternRegistry {
    private static HashMap<ConstructorPattern, ObjectType> patterns = new HashMap<>();

    public static List<ConstructorPattern> getPossiblePatterns() {
        List<ConstructorPattern> list = new ArrayList<>();
        patterns.keySet().forEach(pattern -> list.add(pattern));
        return list;
    }

    public static ObjectType getObjectType(ConstructorPattern pattern) {
        return patterns.get(pattern);
    }

    public static void addPattern(ConstructorPattern pattern, ObjectType objectType) {
        patterns.put(pattern, objectType);
    }

    public static void setup() {
        //patterns work any size from 1x1 to 7x7 and will trim rows/columns of 0s
        addPattern(new ConstructorPattern(new int[][]{
                {0, 1, 1, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 0, 1}
        }), ObjTypeRegistry.PICKAXE_HEAD);
        addPattern(new ConstructorPattern(new int[][]{
                {0, 1, 1, 1},
                {1, 0, 0, 0}
        }), ObjTypeRegistry.HOE_HEAD);
        addPattern(new ConstructorPattern(new int[][]{
                {0, 1, 0},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        }), ObjTypeRegistry.SWORD_BLADE);
        addPattern(new ConstructorPattern(new int[][]{
                {1},
                {1},
                {1},
                {1},
                {1}
        }), ObjTypeRegistry.TOOL_ROD);
        addPattern(new ConstructorPattern(new int[][]{
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}
        }), ObjTypeRegistry.BINDING);
        addPattern(new ConstructorPattern(new int[][]{
                {1, 0, 0, 0, 0},
                {1, 1, 1, 0, 0},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 0, 0},
                {1, 0, 0, 0, 0}
        }), ObjTypeRegistry.AXE_HEAD);
        addPattern(new ConstructorPattern(new int[][]{
                {1, 1, 1, 1, 1},
                {0, 1, 1, 1, 0}
        }), ObjTypeRegistry.SWORD_GUARD);
        addPattern(new ConstructorPattern(new int[][]{
                {1, 0, 0, 0, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 1, 0}
        }), ObjTypeRegistry.WRENCH_HEAD);
        addPattern(new ConstructorPattern(new int[][]{
                {0, 1, 1, 0},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        }), ObjTypeRegistry.SHOVEL_HEAD);
    }
}
