package me.pixeldots.scriptedmodels.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.mixin.IAnimalModelMixin;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import me.pixeldots.scriptedmodels.script.line.Line;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class ScriptedModelsGUI extends GuiHandler {

    public ModelPart selected;

    public ScriptedModelsGUI() {
        super("Scripted Models");
    }
    
    @Override
    public void init() {    
        LivingEntity entity = ScriptedModels.minecraft.player;
        AnimalModel<?> model = (AnimalModel<?>)ScriptedModels.EntityModels.get(entity);
        UUID uuid = entity.getUuid();

        addButton(new ButtonWidget(5, 5, 100, 20, Text.of("remove script"), (btn) -> {
            if (selected == null) ScriptedModels.EntityScript.get(uuid).global = new Line[0];
            else ScriptedModels.EntityScript.get(uuid).parts.remove(selected);
        }));
        addButton(new ButtonWidget(110, 5, 100, 20, Text.of("reset"), (btn) -> {
            ScriptedModels.EntityScript.remove(entity.getUuid());
        }));

        int index = 0;
        for (ModelPart part : ((IAnimalModelMixin)model).getBodyParts()) {
            addButton(new ButtonWidget(110, index*25+35, 100, 20, Text.of("" + index), (btn) -> {
                selected = part;
            }));
            index++;
        }

        int index2 = 0;
        for (ModelPart part : ((IAnimalModelMixin)model).getHeadParts()) {
            addButton(new ButtonWidget(220, index2*25+35, 100, 20, Text.of("" + index2), (btn) -> {
                selected = part;
            }));
            index2++;
        }

        int y = 0;
        File directory = new File(ScriptedModels.minecraft.runDirectory.getAbsolutePath() + ScriptedModels.ScriptsPath);
        for (File file : directory.listFiles()) {
            addButton(new ButtonWidget(5, y*25+35, 100, 20, Text.of(file.getName()), (btn) -> {
                readFile(file, uuid);
            }));
            y++;
        }
    }

    public void readFile(File file, UUID uuid) {
        if (!ScriptedModels.EntityScript.containsKey(uuid))
            ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            String[] split = new String(stream.readAllBytes()).split("\n");

            if (selected == null) ScriptedModels.EntityScript.get(uuid).global = Interpreter.compile(split);
            else ScriptedModels.EntityScript.get(uuid).parts.put(selected, Interpreter.compile(split));
        } catch (IOException ex) {
        } finally {
            try { stream.close(); } catch (IOException e) {}
        }
    }

}
