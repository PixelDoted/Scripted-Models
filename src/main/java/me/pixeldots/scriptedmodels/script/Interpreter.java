package me.pixeldots.scriptedmodels.script;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.pixeldots.scriptedmodels.script.line.Line;
import me.pixeldots.scriptedmodels.script.line.LineType;

public class Interpreter {
    
    /**
     * Compiles a list of lines
     * @param lines the scripts lines
     * @return the compiled script
     */
    public static List<Line> compile(String[] lines) {
        List<Line> output = new CopyOnWriteArrayList<>();
        for (String line : lines) {
            Line compiled = compile_line(line);
            if (compiled == null) continue; // skip if the compilation failed

            output.add(compiled);
        }
        return output;
    }

    /**
     * Compiles a line
     * @param line the line to compile
     * @return the compiled line
     */
    public static Line compile_line(String line) {
        String[] args = split(line); // split the line into it's arguments
        Object[] data = new Object[args.length <= 1 ? 0 : args.length-1];
        for (int i = 1; i < args.length; i++) {
            data[i-1] = getVariableType(args[i]); // get variables from the arguments
        }
        LineType type = LineType.getType(args[0]); // get the Line Type from the first argument
        if (type == null) return null; // return null if it failed

        return new Line(type, data);
    }

    /**
     * Decompiles a compiled script
     * @param script the script to decompile
     * @return the decompiled script
     */
    public static String decompile(List<Line> script) {
        String output = "";
        for (Line line : script) {
            String decompiled = line.type.toString();
            for (Object variable : line.data) {
                decompiled += " " + variable.toString();
            }
            output += decompiled + "\n";
        }
        return output.trim();
    }

    /**
     * Decompiles a compiled line
     * @param line the line to decompile
     * @return the decompiled line
     */
    public static String decompile_line(Line line) {
        String decompiled = line.type.toString();
        for (Object variable : line.data) {
            decompiled += " " + variable.toString();
        }
        return decompiled;
    }

    private static Object getVariableType(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length()-1);
        } else {
            float out = calculate(value);
            if (out != Float.MAX_VALUE) return out;
        }
        return value;
    }

    // Utillities
    private static String[] split(String input) { // splits the string at all spaces, unless in quotation marks
        List<String> output = new ArrayList<>();
        
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(input);
        while (regexMatcher.find()) {
            output.add(regexMatcher.group());
        }

        if (output.size() == 0) return new String[] { input };
        return output.toArray(new String[output.size()]);
    }

    private static float calculate(String value) {
        if (PostfixOperation.isNumeric(value)) return Float.parseFloat(value);
        else {
            try {
                return (float)PostfixOperation.evaluate(value);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return Float.MAX_VALUE;
    }

}
