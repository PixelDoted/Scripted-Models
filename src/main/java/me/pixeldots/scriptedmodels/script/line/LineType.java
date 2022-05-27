package me.pixeldots.scriptedmodels.script.line;

import me.pixeldots.scriptedmodels.platform.FabricFunctions;

public enum LineType {
    PARTICLE(LineMode.TICK, (data, extras) -> {
        FabricFunctions.particle(extras, getS(data, 0), getF(data, 1), getF(data, 2),
            getF(data, 3), getF(data, 4), getF(data, 5), getF(data, 6));
    }),
    VERTEX(LineMode.RENDER, (data, extras) -> { 
        FabricFunctions.vertex(extras, getF(data, 0), getF(data, 1), getF(data, 2),
            getF(data, 3), getF(data, 4), getF(data, 5), getF(data, 6),
            getF(data, 7), getF(data, 8), getF(data, 9), getF(data, 10),
            getF(data, 11)); 
    }),
    TRANSLATE(LineMode.RENDER, (data, extras) -> { 
        FabricFunctions.translate(extras, getF(data, 0), getF(data, 1), getF(data, 2)); 
    }),
    ROTATE(LineMode.RENDER, (data, extras) -> { 
        FabricFunctions.rotate(extras, getF(data, 0), getF(data, 1), getF(data, 2)); 
    }),
    SCALE(LineMode.RENDER, (data, extras) -> { 
        FabricFunctions.scale(extras, getF(data, 0), getF(data, 1), getF(data, 2)); 
    }),
    CANCEL(LineMode.GLOBAL, (data, extras) -> {});

    LineMode mode;
    LineFunc func;

    private LineType(LineMode mode, LineFunc func) {
        this.func = func;
        this.mode = mode;
    }

    public static LineType getType(String s) {
        s = getLongType(s);
        for (LineType lineType : values()) {
            if (lineType.name().equalsIgnoreCase(s)) return lineType;
        }
        return null;
    }

    public static String getLongType(String s) {
        switch (s) {
            case "v":
                return "VERTEX";
            case "p":
                return "PARTICLE";
            default:
                return s;
        }
    }

    private static float getF(Object[] data, int index) {
        return LineUtils.getFloat(data, index);
    }
    private static String getS(Object[] data, int index) {
        return LineUtils.getString(data, index);
    }

    public interface LineFunc {
        void run(Object[] data, Object[] extras);
    }
}