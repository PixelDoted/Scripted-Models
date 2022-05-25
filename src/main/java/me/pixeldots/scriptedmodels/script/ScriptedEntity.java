package me.pixeldots.scriptedmodels.script;

import java.util.HashMap;
import java.util.Map;

import me.pixeldots.scriptedmodels.script.line.Line;
import net.minecraft.client.model.ModelPart;

public class ScriptedEntity {
    
    public boolean show_NameTag = true;
    public Line[] global;

    public Map<ModelPart, Line[]> parts;

    public ScriptedEntity() {
        parts = new HashMap<>();
        global = new Line[0];
    }

}
