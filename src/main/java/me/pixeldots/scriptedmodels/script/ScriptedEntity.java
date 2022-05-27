package me.pixeldots.scriptedmodels.script;

import java.util.HashMap;
import java.util.Map;

import me.pixeldots.scriptedmodels.script.line.Line;
import net.minecraft.client.model.ModelPart;

public class ScriptedEntity {
    
    public Line[] global; // the entity's global script
    public Map<ModelPart, Line[]> parts;

    public ScriptedEntity() {
        global = new Line[0];
        parts = new HashMap<>();
    }

}
