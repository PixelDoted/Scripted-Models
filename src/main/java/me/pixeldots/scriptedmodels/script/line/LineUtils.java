package me.pixeldots.scriptedmodels.script.line;

public class LineUtils {
    
    public static String getString(Object[] data, int index) {
        if (data.length > index && data[index] instanceof String) 
            return (String)data[index];
        
        return "";
    }

    public static float getFloat(Object[] data, int index) {
        if (data.length > index && data[index] instanceof Float) 
            return (float)data[index];
        
        return 0;
    }

}
