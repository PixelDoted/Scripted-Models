package me.pixeldots.scriptedmodels;

import java.util.ArrayList;
import java.util.UUID;

import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.network.ClientNetwork;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.EntityModel;

public class ClientHelper {

    /**
     * clears the entity's scripts
     * @param uuid the entity's uuid
     */
    public static void reset_entity(UUID uuid) {
        ScriptedModels.EntityScript.remove(uuid);
        ClientNetwork.reset_entity(uuid);
    }

    /**
     * removes a script from an entity
     * @param uuid the entity's uuid
     * @param model the entity's model
     * @param part the entity's modelpart (null for global script)
     */
    public static void remove_script(UUID uuid, EntityModel<?> model, ModelPart part) {
        if (part == null) ScriptedModels.EntityScript.get(uuid).global = new ArrayList<>();
        else ScriptedModels.EntityScript.get(uuid).parts.remove(part);
        ClientNetwork.remove_script(uuid, getModelPartIndex(part, model));
    }

    /**
     * removes a script from an entity
     * @param uuid the entity's uuid
     * @param part the entity's modelpart (null for global script)
     * @param part_id the entity's modelpart id
     */
    public static void remove_script(UUID uuid, ModelPart part, int part_id) {
        if (part == null) ScriptedModels.EntityScript.get(uuid).global = new ArrayList<>();
        else ScriptedModels.EntityScript.get(uuid).parts.remove(part);
        ClientNetwork.remove_script(uuid, part_id);
    }

    /**
     * updates the entity's script and sends a packet to the server
     * @param uuid the entity's uuid
     * @param modelpart the entity's modelpart (null for global script)
     * @param part_id the entity's modelpart id
     * @param script the entity's script
     */
    public static void change_script(UUID uuid, ModelPart modelpart, int part_id, String script) {
        change_script(uuid, modelpart, script);
        ClientNetwork.changed_script(part_id, script, true);
    }

    /**
     * updates the entity's script and sends a packet to the server
     * @param uuid the entity's uuid
     * @param model the entity's model
     * @param modelpart the entity's modelpart (can be 'null')
     * @param script the entity's script
     */
    public static void change_script(UUID uuid, EntityModel<?> model, ModelPart modelpart, String script) {
        change_script(uuid, modelpart, script);
        ClientNetwork.changed_script(getModelPartIndex(modelpart, model), script, true);
    }

    /**
     * decompiles the entity's/modelpart's script
     * @param uuid the entity's uuid
     * @param modelpart the entity's modelpart (null for global script)
     * @return the decompiled script
     */
    public static String decompile_script(UUID uuid, ModelPart modelpart) {
        if (!ScriptedModels.EntityScript.containsKey(uuid))
            return "";
        
        if (modelpart == null) return Interpreter.decompile(ScriptedModels.EntityScript.get(uuid).global);
        else {
            if (!ScriptedModels.EntityScript.get(uuid).parts.containsKey(modelpart)) return "";
            return Interpreter.decompile(ScriptedModels.EntityScript.get(uuid).parts.get(modelpart));
        }
    }

    private static void change_script(UUID uuid, ModelPart modelpart, String script) {
        if (!ScriptedModels.EntityScript.containsKey(uuid))
            ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());

        if (modelpart == null) ScriptedModels.EntityScript.get(uuid).global = Interpreter.compile(script.split("\n"));
        else ScriptedModels.EntityScript.get(uuid).parts.put(modelpart, Interpreter.compile(script.split("\n")));
    }

    private static int getModelPartIndex(ModelPart modelpart, EntityModel<?> model) {
        int index = 100;

        for (ModelPart part : PlatformUtils.getHeadParts(model)) {
            if (modelpart == part) return index;
            index++;
        }

        index = 0;
        for (ModelPart part : PlatformUtils.getBodyParts(model)) {
            if (modelpart == part) return index;
            index++;
        }

        return -1;
    }

}
