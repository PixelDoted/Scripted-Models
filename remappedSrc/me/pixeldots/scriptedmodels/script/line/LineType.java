package me.pixeldots.scriptedmodels.script.line;

import me.pixeldots.scriptedmodels.script.FabricFunctions;

import net.minecraft.client.render.VertexConsumer;

public enum LineType {
    VERTEX((data, vc) -> { 
        FabricFunctions.vertex((VertexConsumer)vc, (float)data[0], (float)data[1], (float)data[2], (float)data[3], (float)data[4], (float)data[5], (float)data[6], (float)data[7], (int)data[8], (int)data[9], (int)data[10], (int)data[11]); 
    });

    LineFunc func;

    private LineType(LineFunc func) {
        this.func = func;
    }

    public interface LineFunc {
        void run(Object[] data, Object extra);
    }
}