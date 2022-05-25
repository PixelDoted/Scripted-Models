package me.pixeldots.scriptedmodels.platform.other;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.FabricUtils;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import me.pixeldots.scriptedmodels.script.Interpreter;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import me.pixeldots.scriptedmodels.script.line.Line;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class ScriptedModelsGUI extends GuiHandler {

    public ModelPart selected = null;

    public ScriptedModelsGUI() {
        super("Scripted Models");
    }
    
    @Override
    public void init() {    
        LivingEntity entity = ScriptedModels.minecraft.player;
        AnimalModel<?> model = (AnimalModel<?>)FabricUtils.getModel(entity);
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

        index = 0;
        for (ModelPart part : ((IAnimalModelMixin)model).getHeadParts()) {
            addButton(new ButtonWidget(220, index*25+35, 100, 20, Text.of("" + index), (btn) -> {
                selected = part;
            }));
            index++;
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
            ex.printStackTrace();
        } finally {
            try { stream.close(); } catch (IOException e) {}
        }
    }

}
