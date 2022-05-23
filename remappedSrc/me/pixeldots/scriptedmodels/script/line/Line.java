package me.pixeldots.scriptedmodels.script.line;

public class Line {
    
    public LineType type;
    public Object[] data;

    public void run(Object extra) {
        type.func.run(data, extra);
    }

    public Line(String _type, Object[] _data) {
        type = LineType.valueOf(_type);
        data = _data;
    }
    
}
