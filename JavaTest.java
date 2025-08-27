package com.hlb;

import java.text.SimpleDateFormat;
import java.util.*;

class JavaTest {
    // E.g. Input: Automation, Output: noitamotuA
    public static String reverseStringWithoutUsingStringMethod(String s) {
        char[] arr = s.toCharArray();
        int left = 0, right = arr.length - 1;
        while (left < right) {
            char temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
        return new String(arr);
    }

    // Sort the integer in ASC order without using the built-in method such as ArrayUtils.sort
    public static Integer[] sortIntegers(Integer[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

    // Check if the given date is within the date range
    public static boolean isInDateRange(Date givenDate, Date startDate, Date endDate) {
        return !givenDate.before(startDate) && !givenDate.after(endDate);
    }

    // sort the given String in ASC order without using method like Arrays.sort
    public static char[] sortStringInAscOrder(String input) {
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            for (int j = 0; j < chars.length - i - 1; j++) {
                if (chars[j] > chars[j + 1]) {
                    char temp = chars[j];
                    chars[j] = chars[j + 1];
                    chars[j + 1] = temp;
                }
            }
        }
        return chars;
    }

    // Given a Alphanumeric, please return a character with the lowest occurrence
    // E.g. AbcdAbc123123, the answer is d as it only occurs once
    // If there is more than 1 char with the same lowest occurrence, return the first char in ASC order
    public static char lowestOccurrence(String input) {
        HashMap<Character, Integer> freqMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        int min = Integer.MAX_VALUE;
        char result = 0;

        for (char c : freqMap.keySet()) {
            int count = freqMap.get(c);
            if (count < min || (count == min && c < result)) {
                min = count;
                result = c;
            }
        }
        return result;
    }

    // Emulate a calculator to resolve the given expression
    // Abide by the mathematics rules such as (), *, / have higher precedence than +, -
    // Please don't use any utility such as script engine, you need to implement the calculator logics
    // E.g.: expression: 2.3 * (3 + 4) + 5 / -0.3
    //       calculated: -0.56666666666667
    public static double solveEquations(String expression) {
        List<String> tokens = tokenize(expression);
        List<String> postfix = infixToPostfix(tokens);
        return evaluatePostfix(postfix);
    }
    // Tokenize (handles negative numbers + parentheses)
    private static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder num = new StringBuilder();

        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isWhitespace(c)) continue;

            if (Character.isDigit(c) || c == '.') {
                num.append(c);
            } else if (c == '-' && (i == 0 || "()+-*/".indexOf(expr.charAt(i - 1)) >= 0)) {
                // unary minus → part of number
                num.append(c);
            } else {
                if (num.length() > 0) {
                    tokens.add(num.toString());
                    num.setLength(0);
                }
                tokens.add(String.valueOf(c));
            }
        }
        if (num.length() > 0) {
            tokens.add(num.toString());
        }
        return tokens;
    }

    // Shunting Yard (Infix → Postfix)
    private static List<String> infixToPostfix(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        for (String token : tokens) {
            if (isNumber(token)) {
                output.add(token);
            } else if ("+-*/".contains(token)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop(); // pop "("
            }
        }
        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }
        return output;
    }

    // Evaluate postfix
    private static double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();
        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+" -> stack.push(a + b);
                    case "-" -> stack.push(a - b);
                    case "*" -> stack.push(a * b);
                    case "/" -> stack.push(a / b);
                }
            }
        }
        return stack.pop();
    }

    private static boolean isNumber(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static int precedence(String op) {
        if (op.equals("+") || op.equals("-")) return 1;
        if (op.equals("*") || op.equals("/")) return 2;
        return 0;
    }

    private static double evaluate(String expr) {
        List<String> tokens = new ArrayList<>();
        StringBuilder num = new StringBuilder();
        boolean expectNumber = true;  // Flag to allow leading negative numbers

        for (char c : expr.toCharArray()) {
            if ("+-*/".indexOf(c) >= 0) {
                if (expectNumber && c == '-') {
                    // It's a negative number
                    num.append(c);
                    expectNumber = false;
                } else {
                    if (num.length() > 0) {
                        tokens.add(num.toString());
                    }
                    tokens.add(Character.toString(c));
                    num = new StringBuilder();
                    expectNumber = true;
                }
            } else {
                num.append(c);
                expectNumber = false;
            }
        }

        if (num.length() > 0) {
            tokens.add(num.toString());
        }

        double result = Double.parseDouble(tokens.get(0));
        for (int i = 1; i < tokens.size(); i += 2) {
            String op = tokens.get(i);
            double next = Double.parseDouble(tokens.get(i + 1));
            switch (op) {
                case "+" -> result += next;
                case "-" -> result -= next;
                case "*" -> result *= next;
                case "/" -> result /= next;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + reverseStringWithoutUsingStringMethod("Automation"));
        Integer[] intArray = new Integer[]{10, 12, 54, 1, 2, -9, 8};
        Integer[] sortedArr = sortIntegers(intArray);
        System.out.print("Test 2: ");
        for (Integer i : sortedArr) {
            System.out.print(i + ", ");
        }

        System.out.println();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date startDate = sdf.parse("2024-12-01 13:09:22");
            Date endDate = sdf.parse("2025-01-09 20:10:12");
            Date givenDate = sdf.parse("2025-01-02 00:11:22");
            System.out.println("Test 3: " + isInDateRange(givenDate, startDate, endDate));
        } catch (Exception e) {
            System.out.println(e);
        }

        char[] sorted = sortStringInAscOrder("testingNG311");
        System.out.print("Test 4 :");
        for (char c : sorted) {
            System.out.print(c + ", ");
        }
        System.out.println();
        System.out.println("Test 5: " + lowestOccurrence("AbcdAbc123123"));
        System.out.print("Test 6: ");
        double calculated = solveEquations("3.4 * (-5.5 + 2.45) / 0.23 - 8.82 + 78.888");
        System.out.print("calculated = " + calculated);
    }
}
