package me.pixeldots.scriptedmodels.platform.network;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class SMConfig {
    
    public SMConfig(ForgeConfigSpec.Builder builder) {
        ConfigValue<Integer> _maximumscriptlinecount = builder.define("Server.MaximumScriptLineCount", 0);
        ConfigValue<Integer> _compressthreshold = builder.define("Server.CompressThreshold", -1);
        
        ScriptedModelsMain.MaximumScriptLineCount = _maximumscriptlinecount.get();
        ScriptedModelsMain.CompressThreshold = _compressthreshold.get();
    }

}
