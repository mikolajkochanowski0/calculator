package org.example.calculator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ExpressionCalculatorTest {

    @Test
    void testSimpleAddition() {
        assertEquals(5, ExpressionCalculator.calculate("2 + 3"));
    }

    @Test
    void testSimpleSubtraction() {
        assertEquals(-1, ExpressionCalculator.calculate("2 - 3"));
    }

    @Test
    void testSimpleMultiplication() {
        assertEquals(6, ExpressionCalculator.calculate("2 * 3"));
    }

    @Test
    void testSimpleDivision() {
        assertEquals(2, ExpressionCalculator.calculate("4 / 2"));
    }

    @Test
    void testDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> ExpressionCalculator.calculate("4 / 0"));
    }

    @Test
    void testMultipleOperatorsWithPrecedence() {
        assertEquals(11, ExpressionCalculator.calculate("3 + 2 * 4"));
    }

    @Test
    void testExpressionWithNegativeNumber() {
        assertEquals(0, ExpressionCalculator.calculate("3 * -2 + 6"));
    }

    @Test
    void testComplexExpression() {
        assertEquals(2, ExpressionCalculator.calculate("10 + 2 * -3 - 4 / 2"));
    }

    @Test
    void testEmptyExpression() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionCalculator.calculate(""));
    }

    @Test
    void testNullExpression() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionCalculator.calculate(null));
    }

    @Test
    void testInvalidOperator() {
        assertThrows(IllegalArgumentException.class, () -> ExpressionCalculator.calculate("2 ^ 3"));
    }

    @Test
    void testOnlyNegativeNumber() {
        assertEquals(-5, ExpressionCalculator.calculate("-5"));
    }
}
