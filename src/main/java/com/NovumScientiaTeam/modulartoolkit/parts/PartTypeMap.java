package com.NovumScientiaTeam.modulartoolkit.parts;

import com.EmosewaPixel.pixellib.materialsystem.types.ObjectType;
import com.NovumScientiaTeam.modulartoolkit.parts.partTypes.PartType;

import java.util.HashMap;
import java.util.Map;

public final class PartTypeMap {
    private static final Map<ObjectType, PartType> MAP = new HashMap<>();

    public static void bindToPartType(ObjectType obj, PartType part) {
        MAP.put(obj, part);
    }

    public static PartType getPartType(ObjectType type) {
        return MAP.get(type);
    }
}
