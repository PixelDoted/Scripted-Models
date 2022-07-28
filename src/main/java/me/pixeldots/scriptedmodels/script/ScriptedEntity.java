package me.pixeldots.scriptedmodels.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import me.pixeldots.scriptedmodels.script.line.Line;
import net.minecraft.client.model.geom.ModelPart;

public class ScriptedEntity {
    
    public List<Line> global; // the entity's global script
    public Map<ModelPart, List<Line>> parts;

    public ScriptedEntity() {
        global = new CopyOnWriteArrayList<>();
        parts = new HashMap<>();
    }

    public void append(List<Line> lines, ModelPart part) {
        List<Line> lParts = null;
        if (part == null) lParts = global;
        else if (parts.containsKey(part)) lParts = parts.get(part);

        if (lParts == null) return;
        for (Line line : lines) { 
            lParts.add(line); 
        }
    }

    public void remove(int start, int end, ModelPart part) {
        List<Line> lParts = null;
        if (part == null) lParts = global;
        else if (parts.containsKey(part)) lParts = parts.get(part);

        if (lParts == null) return;
        for (int i = start; i < end; i++) {
            lParts.remove(start);
        }
    }

}
