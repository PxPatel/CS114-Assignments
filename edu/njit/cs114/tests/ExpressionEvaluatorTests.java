package edu.njit.cs114.tests;

import edu.njit.cs114.ExpressionEvaluator;
import edu.njit.cs114.ExpressionToken;
import edu.njit.cs114.OperandToken;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Author: Ravi Varadarajan
 * Date created: 3/4/2024
 */
public class ExpressionEvaluatorTests extends UnitTests {

    @Test
    public void testUnbalancedParentheses() {
        try {
            List<ExpressionToken> infix = ExpressionEvaluator.parseExpr(" ( 2 % 7 + ( 6 * 3 ) ** 4");
            List<ExpressionToken> postfix = ExpressionEvaluator.convertToPostFix(infix);
            failure("testUnbalancedParentheses", new Exception("Unbalanced parentheses not recognized"));
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
            success("testConvertInfixToPostFix1");
            totalScore += 5;
        }
    }

    @Test
    public void testCorrectPostFixEval1() {
        try {
            List<ExpressionToken> expr = ExpressionEvaluator.parseExpr(" 8 6 5 * 9 % +");
            assertEquals(ExpressionEvaluator.postFixEval(expr), 11);
            success("testCorrectPostFixEval1");
            totalScore += 5;
        } catch (Exception e) {
            failure("testCorrectPostFixEval1", e);
        }
    }

    @Test
    public void testCorrectPostFixEval2() {
        try {
            List<ExpressionToken> expr = ExpressionEvaluator.parseExpr("7.5 4 -1.5 3.1 2 ** * + -");
            assertTrue(approxEquals(ExpressionEvaluator.postFixEval(expr), 17.915));
            totalScore += 10;
            success("testCorrectPostFixEval2");
        } catch (Exception e) {
            failure("testCorrectPostFixEval2", e);
        }
    }

    @Test
    public void testCorrectPostFixEval3() {
        try {
            List<ExpressionToken> expr = ExpressionEvaluator.parseExpr("-1.67 2.3 4 2 / -5.6 / + 14 5 % - *");
            assertTrue(approxEquals(ExpressionEvaluator.postFixEval(expr), 3.4354));
            totalScore += 10;
            success("testCorrectPostFixEval3");
        } catch (Exception e) {
            failure("testCorrectPostFixEval3", e);
        }
    }

    @Test
    public void testInCorrectPostFixEval4() {
        try {
            List<ExpressionToken> expr = ExpressionEvaluator.parseExpr("-1.67 2.4 / 3.5 * 2.4");
            ExpressionEvaluator.postFixEval(expr);
            failure("testInCorrectPostFixEval4", new Exception("Incorrect expression not recognized"));
            assertTrue(false);
        } catch (Exception e) {
            totalScore += 5;
            success("testInCorrectPostFixEval4");
        }
    }

    @Test
    public void testInCorrectPostFixEval5() {
        try {
            List<ExpressionToken> expr = ExpressionEvaluator.parseExpr("-1.67 2.5 * 3 + -");
            ExpressionEvaluator.postFixEval(expr);
            failure("testInCorrectPostFixEval5", new Exception("Incorrect expression not recognized"));
            assertTrue(false);
        } catch (Exception e) {
            totalScore += 5;
            success("testInCorrectPostFixEval5");
        }
    }

    private boolean AreTokenListsSame(List<ExpressionToken> l1, List<ExpressionToken> l2) {
        if (l1.size() != l2.size()) {
            return false;
        }
        Iterator<ExpressionToken> iter1 = l1.iterator();
        Iterator<ExpressionToken> iter2 = l2.iterator();
        while (iter1.hasNext() & iter2.hasNext()) {
            ExpressionToken tok1 = iter1.next();
            ExpressionToken tok2 = iter2.next();
            if (tok1 instanceof OperandToken) {
                if (!(tok2 instanceof OperandToken)) {
                    return false;
                }
                if (((OperandToken) tok1).getValue() != ((OperandToken) tok2).getValue()) {
                    return false;
                }
            } else {
                if (!tok1.equals(tok2)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void testConvertInfixToPostFix1() {
        try {
            List<ExpressionToken> infix = ExpressionEvaluator.parseExpr("2 % 7 + 6 * 3 ** 4");
            List<ExpressionToken> postfix = ExpressionEvaluator.convertToPostFix(infix);
            List<ExpressionToken> expected = ExpressionEvaluator.parseExpr("2 7 % 6 3 4 ** * +");
            assertTrue(AreTokenListsSame(expected, postfix));
            totalScore += 5;
            success("testConvertInfixToPostFix1");
        } catch (Exception e) {
            failure("testConvertInfixToPostFix1", e);
        }
    }

    @Test
    public void testConvertInfixToPostFix2() {
        try {
            List<ExpressionToken> infix = ExpressionEvaluator.parseExpr("2 + 6 * 3 - 5 + -4");
            List<ExpressionToken> postfix = ExpressionEvaluator.convertToPostFix(infix);
            List<ExpressionToken> expected = ExpressionEvaluator.parseExpr("2 6 3 * + 5 - -4 +");
            assertTrue(AreTokenListsSame(expected, postfix));
            totalScore += 10;
            success("testConvertInfixToPostFix2");
        } catch (Exception e) {
            failure("testConvertInfixToPostFix2", e);
        }
    }

    @Test
    public void testConvertInfixToPostFix3() {
        try {
            List<ExpressionToken> infix = ExpressionEvaluator.parseExpr("( -2 + 6 % 4 ) * ( 3 - 5 )");
            List<ExpressionToken> postfix = ExpressionEvaluator.convertToPostFix(infix);
            List<ExpressionToken> expected = ExpressionEvaluator.parseExpr("-2 6 4 % + 3 5 - *");
            assertTrue(AreTokenListsSame(expected, postfix));
            totalScore += 10;
            success("testConvertInfixToPostFix3");
        } catch (Exception e) {
            failure("testConvertInfixToPostFix3", e);
        }
    }

    @Test
    public void testConvertInfixToPostFix4() {
        try {
            List<ExpressionToken> infix = ExpressionEvaluator.parseExpr("( 2 + ( 6 / ( -3.5 ** ( 9 % 7 ) ) ) -  4 * 5 )");
            List<ExpressionToken> postfix = ExpressionEvaluator.convertToPostFix(infix);
            List<ExpressionToken> expected = ExpressionEvaluator.parseExpr("2 6 -3.5 9 7 % ** / + 4 5 * -");
            assertTrue(AreTokenListsSame(expected, postfix));
            totalScore += 10;
            success("testConvertInfixToPostFix4");
        } catch (Exception e) {
            failure("testConvertInfixToPostFix4", e);
        }
    }

    @Test
    public void testEval1() {
        try {
            assertEquals(ExpressionEvaluator.eval("2 % 7 + 6 * 3 ** 4"), 488);
            totalScore += 5;
            success("testEval1");
        } catch (Exception e) {
            failure("testEval1", e);
        }
    }

    @Test
    public void testEval2() {
        try {
            assertTrue(approxEquals(ExpressionEvaluator.eval("( 2 + ( 6 / ( -3.5 ** ( 9 % 7 ) ) ) -  4 * 5 )"),
                            -17.5102));
            totalScore += 10;
            success("testEval2");
        } catch (Exception e) {
            failure("testEval2", e);
        }
    }

    // @Test
    public void testEvalDirect1() {
        try {
            assertEquals(ExpressionEvaluator.evalDirect("( 2 + 6 ) * ( -3 - 5 / 4 )"), -34);
            totalScore += 4;
            success("testEvalDirect1");
        } catch (Exception e) {
            failure("testEvalDirect1", e);
        }
    }

    // @Test
    public void testEvalDirect2() {
        try {
            assertTrue(approxEquals(ExpressionEvaluator.evalDirect("( 2 + ( 6 / ( -3.5 ** ( 9 % 7 ) ) ) -  4 * 5 )"),
                    -17.5102));
            totalScore += 6;
            success("testEvalDirect2");
        } catch (Exception e) {
            failure("testEvalDirect2", e);
        }
    }

}
