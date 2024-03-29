package me.pixeldots.scriptedmodels.script.line;

public class Line {
    
    public LineType type;
    public Object[] data;

    public void run(Object extras, byte mode) {
        if (type.mode != mode && type.mode != LineMode.GLOBAL) return;
        type.func.run(data, extras);
    }

    public Line(LineType _type, Object[] _data) {
        type = _type;
        data = _data;
    }
    public Line() {}
    
}
