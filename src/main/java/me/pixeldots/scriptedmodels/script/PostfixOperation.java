package me.pixeldots.scriptedmodels.script;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

// http://faculty.cs.niu.edu/~hutchins/csci241/eval.htm
public class PostfixOperation {
    
    private static boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("(") || s.equals(")");
    }

    private static int getPrecedence(String s) {
        switch (s) {
            case "+":
            case "-":
                return 1;

            case "*":
            case "/":
                return 2;

            case "^":
                return 3;
        }
        return -1;
    }

    private static boolean lowerPrecedence(String a, String b) {
        return getPrecedence(a) <= getPrecedence(b);
    }

    public static float evaluate(String input) {
        input = input.trim();
        String a = "";

        char last = ' ';
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if ((c == '-' && last >= '0' && last <= '9') || c == '(' || c == ')' || c == '^' || c == '+' || c == '*' || c == '/') {
               a += " " + c + " ";
            } else a += c;

            last = c;
        }
        
        return evaluate(a.split(" "));
    }

    public static float evaluate(String[] Q) {
        List<String> P = new ArrayList<>();
        Stack<String> S = new Stack<>();

        for (int i = 0; i < Q.length; i++) {
            String q = Q[i];
            if (!isOperator(q)) P.add(q);
            if (q.equals("(")) S.push(q);
            if (q.equals(")"))  {
                while (!S.isEmpty() && !S.peek().equals("(")) {
                    P.add(S.pop());
                }
                S.pop();
            }
            if (isOperator(q)) {
                if (S.isEmpty() || S.peek().equals("(")) {
                    S.push(q);
                } else {
                    while (!S.isEmpty() && lowerPrecedence(q, S.peek())) {
                        String pop = S.pop();
                        if (!pop.equals("(")) P.add(pop);
                        else q = pop;
                    }
                    S.push(q);
                }
            }
        }
        while (!S.isEmpty()) {
            P.add(S.pop());
        }
        
        while (!P.isEmpty()) {
            String c = P.remove(0);

            if (c.equals("(") || c.equals(")")) continue;
            if (!isOperator(c)) S.push(c);

            if (isOperator(c) && !S.isEmpty()) {
                while (S.peek().isEmpty()) { S.pop(); }
                float a = Float.parseFloat(S.pop());
                float b = Float.parseFloat(S.pop());

                float value = b;
                switch (c) {
                    case "+":
                        value += a;
                        break;
                    case "-":
                        value -= a;
                        break;
                    case "/":
                        value /= a;
                        break;
                    case "*":
                        value *= a;
                        break;
                    default:
                        break;
                }

                S.add(String.valueOf(value));
            }
        }

        return Float.parseFloat(S.get(0));
    }

}
