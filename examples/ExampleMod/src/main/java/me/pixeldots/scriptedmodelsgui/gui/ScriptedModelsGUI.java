package me.pixeldots.scriptedmodelsgui.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import me.pixeldots.scriptedmodelsgui.ScriptedModelsGui;
import me.pixeldots.scriptedmodels.ClientHelper;
import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import me.pixeldots.scriptedmodels.platform.mixin.IAnimalModelMixin;
import me.pixeldots.scriptedmodels.script.ScriptedEntity;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class ScriptedModelsGUI extends GuiHandler {

    public ModelPart selected = null;
    public int selected_id = -1;

    public ScriptedModelsGUI() {
        super("Scripted Models");
    }
    
    @Override
    public void init() {
        LivingEntity entity = ScriptedModels.minecraft.player;
        AgeableListModel<?> model = (AgeableListModel<?>)PlatformUtils.getModel(entity);
        UUID uuid = entity.getUUID();

        addButton(new Button(5, 5, 100, 20, Component.literal("remove script"), (btn) -> {
            ClientHelper.remove_script(entity.getUUID(), selected, selected_id);
        }));
        addButton(new Button(110, 5, 100, 20, Component.literal("reset"), (btn) -> {
            ClientHelper.reset_entity(entity.getUUID());
        }));
        addButton(new Button(220, 5, 100, 20, Component.literal("decompile"), (btn) -> {
            String[] lines = ClientHelper.decompile_script(entity.getUUID(), selected).split("\n");
            for (String line : lines) {
                ScriptedModels.minecraft.player.displayClientMessage(Component.literal(line), false);
            }
        }));

        int index = 0;
        for (ModelPart part : ((IAnimalModelMixin)model).getBodyParts()) {
            final int num = index;
            addButton(new Button(110, index*25+35, 100, 20, Component.literal("" + index), (btn) -> {
                selected = part;
                selected_id = num;
            }));
            index++;
        }

        index = 0;
        for (ModelPart part : ((IAnimalModelMixin)model).getHeadParts()) {
            final int num = index;
            addButton(new Button(220, index*25+35, 100, 20, Component.literal("" + index), (btn) -> {
                selected = part;
                selected_id = num+100;
            }));
            index++;
        }

        int y = 0;
        File directory = new File(ScriptedModels.minecraft.gameDirectory.getAbsolutePath() + ScriptedModelsGui.ScriptsPath);
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                addButton(new Button(5, y*25+35, 100, 20, Component.literal(file.getName()), (btn) -> {
                    readFile(file, uuid, model);
                }));
                y++;
            }
        } else {
            try {
                Files.createDirectory(Path.of(ScriptedModels.minecraft.gameDirectory.getAbsolutePath() + ScriptedModelsGui.ScriptsPath));
            } catch (IOException e) {}
        }
    }

    public void readFile(File file, UUID uuid, AgeableListModel<?> model) {
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
