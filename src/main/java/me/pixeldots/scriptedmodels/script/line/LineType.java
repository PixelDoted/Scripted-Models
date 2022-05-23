package me.pixeldots.scriptedmodels.script.line;

import me.pixeldots.scriptedmodels.script.platform.FabricFunctions;

public enum LineType {
    PARTICLE(LineMode.TICK, (data, extras) -> {
        FabricFunctions.particle(extras, (String)data[0], (float)data[1], (float)data[2], (float)data[3], (float)data[4], (float)data[5], (float)data[6]);
    }),
    VERTEX(LineMode.RENDER, (data, extras) -> { 
        FabricFunctions.vertex(extras, (float)data[0], (float)data[1], (float)data[2], (float)data[3], (float)data[4], (float)data[5], (float)data[6], (float)data[7], (float)data[8], (float)data[9], (float)data[10], (float)data[11]); 
    }),
    TRANSLATE(LineMode.RENDER, (data, extras) -> { 
        FabricFunctions.translate(extras, (float)data[0], (float)data[1], (float)data[2]); 
    }),
    ROTATE(LineMode.RENDER, (data, extras) -> { 
        FabricFunctions.rotate(extras, (float)data[0], (float)data[1], (float)data[2]); 
    }),
    SCALE(LineMode.RENDER, (data, extras) -> { 
        FabricFunctions.scale(extras, (float)data[0], (float)data[1], (float)data[2]); 
    });

    LineMode mode;
    LineFunc func;

    private LineType(LineMode mode, LineFunc func) {
        this.func = func;
        this.mode = mode;
    }

    public static LineType getType(String s) {
        for (LineType lineType : values()) {
            if (lineType.name().equalsIgnoreCase(s)) return lineType;
        }
        return null;
    }

    public interface LineFunc {
        void run(Object[] data, Object[] extras);
    }
}