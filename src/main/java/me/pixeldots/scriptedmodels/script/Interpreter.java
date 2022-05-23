package me.pixeldots.scriptedmodels.script;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.pixeldots.scriptedmodels.script.line.Line;
import me.pixeldots.scriptedmodels.script.line.LineType;

public class Interpreter {
    
    public static Line[] compile(String[] input) {
        List<Line> lines = new ArrayList<>();
        for (String line : input) {
            String[] args = split(line);
            Object[] data = new Object[args.length <= 1 ? 0 : args.length-1];
            for (int i = 1; i < args.length; i++) {
                data[i-1] = getVariableType(args[i]);
            }
            LineType type = LineType.getType(args[0]);
            if (type == null) continue;

            lines.add(new Line(type, data));
        }
        return lines.toArray(new Line[lines.size()]);
    }

    public static Object getVariableType(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length()-1);
        } else if (isNumeric(value)) {
            return Float.parseFloat(value);
            /*if (value.contains(".")) return Float.parseFloat(value);
            else return Integer.parseInt(value);*/
        }
        return value;
    }

    // Utillities
    public static boolean isNumeric(String value) {
        try  {
            Float.parseFloat(value);
            return true;
        } catch (NumberFormatException ex) {}
        return false;
    }

    public static String[] split(String input) { // splits the string at all spaces, unless in quotation marks
        List<String> output = new ArrayList<>();
        
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(input);
        while (regexMatcher.find()) {
            output.add(regexMatcher.group());
        }

        if (output.size() == 0) return new String[] { input };
        return output.toArray(new String[output.size()]);
    }

}
