package org.example.calculator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for evaluating mathematical expressions containing
 * the operators +, -, *, / and handling both positive and negative integers.
 */
public class ExpressionCalculator {

    /**
     * Evaluates the given mathematical expression and returns the result.
     * The expression can include the operators +, -, *, / and both positive
     * and negative integers.
     *
     * @param expression A string representing the mathematical expression, e.g., "3 * -2 + 6"
     * @return The result of the evaluated expression as an integer.
     * @throws IllegalArgumentException If the expression is null, empty, or contains invalid tokens.
     * @throws ArithmeticException      If a division by zero occurs during evaluation.
     */
    public static int calculate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression cannot be null or empty.");
        }

        List<String> tokens = tokenize(expression);
        List<String> rpn = infixToPostfix(tokens);

        return evaluatePostfix(rpn);
    }

    /**
     * Splits the input expression into tokens, ensuring that negative numbers are correctly identified.
     * For example, the expression "-2 + 3" will be tokenized as ["-2", "+", "3"].
     *
     * @param expression The input mathematical expression as a string.
     * @return A list of tokens representing numbers and operators.
     */
    private static List<String> tokenize(String expression) {
        String[] rawTokens = expression.trim().split("\\s+");

        List<String> tokens = new ArrayList<>();
        for (int i = 0; i < rawTokens.length; i++) {
            String token = rawTokens[i];

            if ("-".equals(token)) {
                if (i == 0 || isOperator(rawTokens[i - 1])) {
                    if (i + 1 < rawTokens.length && isNumeric(rawTokens[i + 1])) {
                        tokens.add("-" + rawTokens[i + 1]);
                        i++;
                        continue;
                    } else {
                        throw new IllegalArgumentException("Invalid syntax: '-' must be followed by a number.");
                    }
                }
            }
            tokens.add(token);
        }

        return tokens;
    }

    /**
     * Converts a list of tokens from infix notation to postfix notation (Reverse Polish Notation)
     * using the Shunting Yard algorithm.
     *
     * @param tokens A list of tokens in infix order.
     * @return A list of tokens in postfix (RPN) order.
     * @throws IllegalArgumentException If an unexpected token is encountered.
     */
    private static List<String> infixToPostfix(List<String> tokens) {
        List<String> outputQueue = new ArrayList<>();
        Deque<String> operatorStack = new ArrayDeque<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                outputQueue.add(token);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() &&
                        isOperator(operatorStack.peek()) &&
                        precedence(operatorStack.peek()) >= precedence(token)) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else {
                throw new IllegalArgumentException("Unexpected token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            outputQueue.add(operatorStack.pop());
        }

        return outputQueue;
    }

    /**
     * Evaluates a mathematical expression provided in postfix (Reverse Polish Notation) form.
     *
     * @param rpn A list of tokens representing the expression in postfix order.
     * @return The result of the evaluated expression as an integer.
     * @throws IllegalArgumentException If the expression is malformed or contains insufficient operands.
     * @throws ArithmeticException      If a division by zero occurs.
     */
    private static int evaluatePostfix(List<String> rpn) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (String token : rpn) {
            if (isNumeric(token)) {
                stack.push(Integer.valueOf(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException(
                            "Invalid expression: insufficient operands for operator '" + token + "'.");
                }
                int b = stack.pop();
                int a = stack.pop();

                int result;
                switch (token) {
                    case "+" -> result = a + b;
                    case "-" -> result = a - b;
                    case "*" -> result = a * b;
                    case "/" -> {
                        if (b == 0) {
                            throw new ArithmeticException("Division by zero is not allowed.");
                        }
                        result = a / b;
                    }
                    default -> throw new IllegalArgumentException("Unsupported operator: " + token);
                }
                stack.push(result);
            } else {
                throw new IllegalArgumentException("Unexpected token in RPN expression: " + token);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression: the evaluation stack was not properly reduced.");
        }

        return stack.pop();
    }

    /**
     * Determines if the given token is a recognized operator.
     *
     * @param token The token to evaluate.
     * @return {@code true} if the token is an operator; {@code false} otherwise.
     */
    private static boolean isOperator(String token) {
        return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token);
    }

    /**
     * Checks whether the provided token represents a valid integer.
     *
     * @param token The token to check.
     * @return {@code true} if the token is an integer; {@code false} otherwise.
     */
    private static boolean isNumeric(String token) {
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Retrieves the precedence level of the specified operator.
     * Higher numbers indicate higher precedence.
     *
     * @param operator The operator whose precedence is to be determined.
     * @return An integer representing the operator's precedence.
     * @throws IllegalArgumentException If the operator is unrecognized.
     */
    private static int precedence(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
