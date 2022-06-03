package me.pixeldots.scriptedmodels;

import java.util.UUID;

import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import me.pixeldots.scriptedmodels.platform.network.ClientNetwork;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import me.pixeldots.scriptedmodels.script.line.Line;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;

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
    public static void remove_script(UUID uuid, AnimalModel<?> model, ModelPart part) {
        if (part == null) ScriptedModels.EntityScript.get(uuid).global = new Line[0];
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
        if (part == null) ScriptedModels.EntityScript.get(uuid).global = new Line[0];
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
        ClientNetwork.changed_script(part_id, script);
    }

    /**
     * updates the entity's script and sends a packet to the server
     * @param uuid the entity's uuid
     * @param model the entity's model
     * @param modelpart the entity's modelpart (can be 'null')
     * @param script the entity's script
     */
    public static void change_script(UUID uuid, AnimalModel<?> model, ModelPart modelpart, String script) {
        change_script(uuid, modelpart, script);
        ClientNetwork.changed_script(getModelPartIndex(modelpart, model), script);
    }

    private static void change_script(UUID uuid, ModelPart modelpart, String script) {
        if (!ScriptedModels.EntityScript.containsKey(uuid))
            ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());

        if (modelpart == null) ScriptedModels.EntityScript.get(uuid).global = Interpreter.compile(script.split("\n"));
        else ScriptedModels.EntityScript.get(uuid).parts.put(modelpart, Interpreter.compile(script.split("\n")));
    }

    private static int getModelPartIndex(ModelPart modelpart, AnimalModel<?> model) {
        IAnimalModelMixin mixin = (IAnimalModelMixin)model;
        int index = 100;

        for (ModelPart part : mixin.getHeadParts()) {
            if (modelpart == part) return index;
            index++;
        }

        index = 0;
        for (ModelPart part : mixin.getBodyParts()) {
            if (modelpart == part) return index;
            index++;
        }

        return -1;
    }

}
