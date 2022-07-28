package me.pixeldots.scriptedmodels.script.line;

public class LineUtils {
    
    public static String getString(Object[] data, int index) {
        return (data.length > index && data[index] instanceof String) ? (String)data[index] : "";
    }

    public static float getFloat(Object[] data, int index) {
        return (data.length > index && data[index] instanceof Float) ? (float)data[index] : 0;
    }

}
