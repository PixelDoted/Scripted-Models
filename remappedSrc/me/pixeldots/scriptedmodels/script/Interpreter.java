package me.pixeldots.scriptedmodels.script;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.pixeldots.scriptedmodels.script.line.Line;

public class Interpreter {
    
    public void run(Line[] script) {
        for (Line line : script) {
            line.run(null);
        }
    }

    public Line[] compile(String[] input) {
        List<Line> lines = new ArrayList<>();
        for (String line : input) {
            String[] args = split(line);
            Object[] data = new Object[args.length-1];
            for (int i = 1; i < args.length; i++) {
                data[i-1] = getVariableType(args[i]);
            }
            lines.add(new Line(args[0], data));
        }
        return lines.toArray(new Line[lines.size()]);
    }

    public Object getVariableType(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length()-1);
        } else if (isNumeric(value)) {
            if (value.contains(".")) return Float.parseFloat(value);
            else return Integer.parseInt(value);
        }
        return value;
    }

    // Utillities
    public boolean isNumeric(String value) {
        try  {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException ex) {}
        return false;
    }

    public String[] split(String input) { // splits the string at all spaces, unless in quotation marks
        List<String> output = new ArrayList<>();
        
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(input);
        while (regexMatcher.find()) {
            output.add(regexMatcher.group());
        }
        return output.toArray(new String[output.size()]);
    }

}
