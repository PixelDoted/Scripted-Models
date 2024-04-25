package me.pixeldotsgui.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import me.pixeldotsgui.ScriptedModelsGui;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class ScriptedModelsGUI extends GuiHandler {

    public ModelPart selected = null;
    public int selected_id = -1;

    public ScriptedModelsGUI() {
        super("Scripted Models");
    }
    
    @Override
    public void init() {    
        LivingEntity entity = ScriptedModels.minecraft.player;
        AnimalModel<?> model = (AnimalModel<?>)PlatformUtils.getModel(entity);
        UUID uuid = entity.getUuid();

        addButton(new ButtonWidget(5, 5, 100, 20, Text.of("remove script"), (btn) -> {
            ClientHelper.remove_script(entity.getUuid(), selected, selected_id);
        }));
        addButton(new ButtonWidget(110, 5, 100, 20, Text.of("reset"), (btn) -> {
            ClientHelper.reset_entity(entity.getUuid());
        }));
        addButton(new ButtonWidget(220, 5, 100, 20, Text.of("decompile"), (btn) -> {
            String[] lines = ClientHelper.decompile_script(entity.getUuid(), selected).split("\n");
            for (String line : lines) {
                MinecraftClient.getInstance().player.sendMessage(Text.of(line), false);
            }
        }));

        int index = 0;
        for (ModelPart part : ((IAnimalModelMixin)model).getBodyParts()) {
            final int num = index;
            addButton(new ButtonWidget(110, index*25+35, 100, 20, Text.of("" + index), (btn) -> {
                selected = part;
                selected_id = num;
            }));
            index++;
        }

        index = 0;
        for (ModelPart part : ((IAnimalModelMixin)model).getHeadParts()) {
            final int num = index;
            addButton(new ButtonWidget(220, index*25+35, 100, 20, Text.of("" + index), (btn) -> {
                selected = part;
                selected_id = num+100;
            }));
            index++;
        }

        int y = 0;
        File directory = new File(ScriptedModels.minecraft.runDirectory.getAbsolutePath() + ScriptedModelsGui.ScriptsPath);
        for (File file : directory.listFiles()) {
            addButton(new ButtonWidget(5, y*25+35, 100, 20, Text.of(file.getName()), (btn) -> {
                readFile(file, uuid, model);
            }));
            y++;
        }
    }

    public void readFile(File file, UUID uuid, AnimalModel<?> model) {
        if (!ScriptedModels.EntityScript.containsKey(uuid))
            ScriptedModels.EntityScript.put(uuid, new ScriptedEntity());

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            //String[] split = new String(stream.readAllBytes()).split("\n");
            
            ClientHelper.change_script(uuid, selected, selected_id, new String(stream.readAllBytes()));
            /*if (selected == null) ScriptedModels.EntityScript.get(uuid).global = Interpreter.compile(split);
            else ScriptedModels.EntityScript.get(uuid).parts.put(selected, Interpreter.compile(split));*/
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try { stream.close(); } catch (IOException e) {}
        }
    }

}
