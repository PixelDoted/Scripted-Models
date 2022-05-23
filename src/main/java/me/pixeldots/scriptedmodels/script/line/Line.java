package me.pixeldots.scriptedmodels.script.line;

public class Line {
    
    public LineType type;
    public Object[] data;

    public void run(Object[] extra, LineMode mode) {
        if (type.mode != mode) return;
        type.func.run(data, extra);
    }

    public Line(LineType _type, Object[] _data) {
        type = _type;
        data = _data;
    }
    public Line() {}
    
}
