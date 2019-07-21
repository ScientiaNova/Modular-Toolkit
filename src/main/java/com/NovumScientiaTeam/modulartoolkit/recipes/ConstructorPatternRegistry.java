package com.NovumScientiaTeam.modulartoolkit.recipes;

import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.NovumScientiaTeam.modulartoolkit.parts.ObjTypeRegistry;

import java.util.*;

public final class ConstructorPatternRegistry {
    private static HashMap<ConstructorPattern, ObjectType> patterns = new HashMap<>();

    public static List<ConstructorPattern> getPossiblePatterns() {
        return new ArrayList<>(patterns.keySet());
    }

    public static ObjectType getObjectType(ConstructorPattern pattern) {
        return patterns.get(pattern);
    }

    public static List<ObjectType> getPossibleObjectTypes() {
        return new ArrayList<>(patterns.values());
    }

    public static ConstructorPattern getPattern(ObjectType type) {
        Optional<Map.Entry<ConstructorPattern, ObjectType>> entry = patterns.entrySet().stream().filter(e -> e.getValue().equals(type)).findFirst();
        if (entry.isPresent())
            return entry.get().getKey();
        return new ConstructorPattern();
    }

    public static void addPattern(ConstructorPattern pattern, ObjectType objectType) {
        patterns.put(pattern, objectType);
    }

    public static void setup() {
        //patterns work any size from 1x1 to 7x7 and will trim rows/columns of 0s
        addPattern(new ConstructorPattern(new byte[][]{
                {0, 1, 1, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 0, 1}
        }), ObjTypeRegistry.PICKAXE_HEAD);
        addPattern(new ConstructorPattern(new byte[][]{
                {0, 1, 1, 1},
                {1, 0, 0, 0}
        }), ObjTypeRegistry.HOE_HEAD);
        addPattern(new ConstructorPattern(new byte[][]{
                {0, 1, 0},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        }), ObjTypeRegistry.SWORD_BLADE);
        addPattern(new ConstructorPattern(new byte[][]{
                {1},
                {1},
                {1},
                {1},
                {1}
        }), ObjTypeRegistry.TOOL_ROD);
        addPattern(new ConstructorPattern(new byte[][]{
                {0, 1, 0},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1},
                {0, 1, 0}
        }), ObjTypeRegistry.TOUGH_TOOL_ROD);
        addPattern(new ConstructorPattern(new byte[][]{
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}
        }), ObjTypeRegistry.BINDING);
        addPattern(new ConstructorPattern(new byte[][]{
                {1, 0, 0, 0, 1},
                {0, 1, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 1, 0, 1, 0},
                {1, 0, 0, 0, 1}
        }), ObjTypeRegistry.TOUGH_BINDING);
        addPattern(new ConstructorPattern(new byte[][]{
                {1, 0, 0, 0, 0},
                {1, 1, 1, 0, 0},
                {1, 1, 1, 1, 1},
                {1, 1, 1, 0, 0},
                {1, 0, 0, 0, 0}
        }), ObjTypeRegistry.AXE_HEAD);
        addPattern(new ConstructorPattern(new byte[][]{
                {1, 1, 1, 1, 1},
                {0, 1, 1, 1, 0}
        }), ObjTypeRegistry.SWORD_GUARD);
        addPattern(new ConstructorPattern(new byte[][]{
                {1, 0, 0, 0, 1},
                {1, 0, 0, 0, 1},
                {1, 1, 0, 1, 1},
                {0, 1, 1, 1, 0}
        }), ObjTypeRegistry.WRENCH_HEAD);
        addPattern(new ConstructorPattern(new byte[][]{
                {0, 1, 1, 0},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1},
                {1, 1, 1, 1}
        }), ObjTypeRegistry.SHOVEL_HEAD);
        addPattern(new ConstructorPattern(new byte[][]{
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        }), ObjTypeRegistry.PLATING);
        addPattern(new ConstructorPattern(new byte[][]{
                {1, 1, 1, 1},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {1, 1, 1, 1}
        }), ObjTypeRegistry.SHIELD_FRAME);
    }
}
