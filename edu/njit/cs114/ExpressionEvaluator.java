package edu.njit.cs114;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Author: Ravi Varadarajan
 * Date created: 3/1/2024
 */
public class ExpressionEvaluator {

    // Use this function for applying operator in postfixEval()
    private static double applyOp(OperatorToken opType, double op1, double op2) {
        switch (opType) {
            case ADD:
                return op1 + op2;
            case SUBTRACT:
                return op1 - op2;
            case MULTIPLY:
                return op1 * op2;
            case DIVIDE:
                return op1 / op2;
            case MOD:
                return op1 % op2;
            case EXP:
                return Math.pow(op1, op2);
            default:
                return 0.0;
        }
    }

    /**
     * Parse an expression into a list of operator and operand tokens
     * 
     * @param str
     * @return
     */
    public static List<ExpressionToken> parseExpr(String str) {
        List<ExpressionToken> expr = new ArrayList<>();
        String[] strTokList = str.split("\\s+");
        for (String strTok : strTokList) {
            String str1 = strTok.trim();
            if (str1.isEmpty()) {
                continue;
            }
            OperatorToken operToken = OperatorToken.opType(str1);
            expr.add(operToken == null ? new OperandToken(str1) : operToken);
        }
        return expr;
    }

    /**
     * Convert Infix expression given as a list of operator and operand tokens to
     * a postfix expression as a list of operator and operand tokens
     * 
     * @param infixExpr
     * @return
     * @throws Exception when the expression is not valid
     *                   such as insufficient number of operators or operands e.g. 4
     *                   * 2 5, 4 * 2 +
     *                   or not having balanced parentheses e.g. (4 * ( 5 + 3 )
     */
    public static List<ExpressionToken> convertToPostFix(List<ExpressionToken> infixExpr) throws Exception {
        Stack<OperatorToken> operatorStack = new Stack<>();
        List<ExpressionToken> postfixExpr = new ArrayList<>();

        for (int i = 0; i < infixExpr.size(); i++) {
            ExpressionToken token = infixExpr.get(i);

            if (token instanceof OperandToken) {
                // If token is operand, add it directly to the postfix expression
                postfixExpr.add(token);
            } else if (token instanceof OperatorToken) {
                OperatorToken operator = (OperatorToken) token;
                if (operator == OperatorToken.OPENPAR) {
                    // If token is open parenthesis, push it onto the stack
                    operatorStack.push(operator);
                } else if (operator == OperatorToken.CLOSEDPAR) {
                    // If token is close parenthesis, pop operators from the stack and add to
                    // postfix expression until an open parenthesis is encountered
                    while (!operatorStack.isEmpty() && operatorStack.peek() != OperatorToken.OPENPAR) {
                        postfixExpr.add(operatorStack.pop());
                    }
                    if (operatorStack.isEmpty()) {
                        throw new Exception("Unbalanced parentheses");
                    }
                    operatorStack.pop(); // Discard the open parenthesis
                } else {
                    // If token is an operator
                    while (!operatorStack.isEmpty() && !(operator.precedes(operatorStack.peek()))) {
                        // Pop operators from stack and add to postfix expression while the current
                        // operator token has a lower or equal precedence than the top of stack
                        postfixExpr.add(operatorStack.pop());
                    }
                    // Push the current operator onto the stack
                    operatorStack.push(operator);
                }
            } else {
                throw new Exception("Invalid token type");
            }

        }

        // Add remaining operators into the postFix result
        while (!operatorStack.isEmpty()) {
            if (operatorStack.peek() == OperatorToken.OPENPAR || operatorStack.peek() == OperatorToken.CLOSEDPAR) {
                throw new Exception("Unbalanced parentheses");
            }
            postfixExpr.add(operatorStack.pop());
        }

        return postfixExpr;

    }

    /**
     * Evaluate post fix expression given as a list of operator and operand tokens
     * and return the result
     * 
     * @param postfixExpr
     * @return
     * @throws Exception when the expression is not valid
     *                   such as insufficient number of operators or operands e.g. 4
     *                   5 2 *, 4 *
     */
    public static double postFixEval(List<ExpressionToken> postfixExpr) throws Exception {
        Stack<Double> stack = new Stack<>();

        for (int i = 0; i < postfixExpr.size(); i++) {
            ExpressionToken token = postfixExpr.get(i);

            if (token instanceof OperandToken) {
                // If token is operand, push its value onto the stack
                stack.push(((OperandToken) token).getValue());
            } else if (token instanceof OperatorToken) {
                // If token is operator, pop operands from the stack, perform operation, and
                // push result back onto the stack
                OperatorToken operator = (OperatorToken) token;

                if (stack.size() < 2) {
                    throw new Exception("Insufficient operands for operator: " + operator.symbol);
                }

                double oper2 = stack.pop();
                double oper1 = stack.pop();
                double result = applyOp(operator, oper1, oper2);
                stack.push(result);
            } else {
                throw new Exception("Invalid token type");
            }
        }

        // If there is not only 1 remaining value in stack, throw error
        if (stack.size() != 1) {
            throw new Exception("Invalid expression: Too many operands or operators");
        }

        return stack.pop();
    }

    /**
     * Evaluate an infix expression string using postfix
     * 
     * @param str
     * @return
     * @throws Exception when the expression is not valid (e.g 2 + 3 5)
     */
    public static double eval(String str) throws Exception {
        return postFixEval(convertToPostFix(parseExpr(str)));
    }

    /**
     * Evaluate an infix expression string directly
     * 
     * @param str
     * @return
     * @throws Exception when an expression is invalid
     */
    public static double evalDirect(String str) throws Exception {
        List<ExpressionToken> tokens = parseExpr(str);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        /**
         * Uncomment commented lines below after you have finished the homework
         * implementations
         **/
        System.out.println(String.format("postfix notation for %s : %s", "-8.5",
                convertToPostFix(parseExpr("-8.5"))));
        double result = postFixEval(parseExpr("-8.5"));
        System.out.println(String.format("postfix expr. %s evaluated as %.4f", "-8.5",
                result));
        assert Math.abs(result - (-8.5)) <= 0.00001;
        System.out.println(String.format("%s evaluated as %.4f", "-8.5",
                eval("-8.5")));
        System.out.println(String.format("postfix notation for %s : %s", " 5 * -2",
                convertToPostFix(parseExpr(" 5 * -2"))));
        result = postFixEval(parseExpr(" 5 -2 *"));
        System.out.println(String.format("postfix expr. %s evaluated as %.4f", " 5 -2 *",
                result));
        assert Math.abs(result - (-10.0)) <= 0.00001;
        System.out.println(String.format("%s evaluated as %.4f", " -2 * 5",
                eval(" -2 * 5")));
        System.out.println(String.format("postfix notation for %s : %s", "( 4 + -2 ) * 7",
                convertToPostFix(parseExpr("( 4 + -2 ) * 7"))));
        System.out.println(String.format("%s evaluated as %.4f", "( 4 + -2 ) * 7",
                eval("( 4 + -2 ) * 7")));
        try {
            System.out.println(String.format("postfix notation for %s : %s", " 4 * ( 3 - 2 ) ) ** -2 ",
                    convertToPostFix(parseExpr(" 4 * ( 3 - 2 ) ) ** -2 "))));
        } catch (Exception e) {
            System.out.println("Invalid infix expression : 4 * ( 3 - 2 ) ) ** -2 ");
        }
        try {
            System.out.println(String.format("%s evaluated as %.4f", " 4 * ( 3 - 2 ) ) ** -2",
                    eval(" 4 * ( 3 - 2 ) ) ** -2")));
        } catch (Exception e) {
            System.out.println("Invalid infix expression : 4 * ( 3 - 2 ) ) ** -2 ");
        }
        System.out.println(String.format("postfix notation for %s : %s",
                " ( ( 2.5 + 3.1 ) ** 2  - 6 ) * -3.4 ",
                convertToPostFix(parseExpr(" ( ( 2.5 + 3.1 ) ** 2  - 6 ) * -3.4 "))));
        result = postFixEval(parseExpr("2.5 3.1 + 2 ** 6 - -3.4 *"));
        System.out.println(String.format("postfix expr. %s evaluated as %.4f",
                "2.5 3.1 + 2 ** 6 - -3.4 *", result));
        assert Math.abs(result - (-86.224)) <= 0.00001;
        System.out.println(String.format("%s evaluated as %.4f",
                " ( ( 2.5 + 3.1 ) ** 2  - 6 ) * -3.4",
                eval(" ( ( 2.5 + 3.1 ) ** 2  - 6 ) * -3.4")));
        result = postFixEval(parseExpr("4 -2 5 * +"));
        System.out.println(String.format("postfix expr. %s evaluated as %.4f", "4 -2 5 * +",
                result));
        assert Math.abs(result - (-6.0)) <= 0.00001;
        result = postFixEval(parseExpr("3.5 2 3 + /"));
        System.out.println(String.format("postfix expr. %s evaluated as %.4f", "3.5 2 3 + /",
                result));
        assert Math.abs(result - (0.7)) <= 0.00001;
        System.out.println(String.format("%s evaluated as %.4f", "3.5 / ( 2 + 3 )",
                eval("3.5 / ( 2 + 3 )")));
        System.out.println(String.format("postfix notation for %s : %s", "2 ** 3 ** 2",
                convertToPostFix(parseExpr("2 ** 3 ** 2"))));
        System.out.println(String.format("%s evaluated as %.4f", "2 ** 3 ** 2",
                eval("2 ** 3 ** 2")));
        System.out.println(String.format("postfix expr. %s evaluated as %.4f",
                "46 3 8 * 5 - % 10 -",
                postFixEval(parseExpr("46 3 8 * 5 - % 10 -"))));
        System.out.println(String.format("postfix notation for %s : %s", "46 % ( 3 * 8 - 5 ) - 10",
                convertToPostFix(parseExpr("46 % ( 3 * 8 - 5 ) - 10"))));
        System.out.println(String.format("%s evaluated as %.4f", "46 % ( 3 * 8 - 5 ) - 10",
                eval("46 % ( 3 * 8 - 5 ) - 10")));
        System.out.println(String.format("expr. %s evaluated as %.4f", "( 8 - 6 ) * ( 6 / 2 ) + 3",
                eval("( 8 - 6 ) * ( 6 / 2 ) + 3")));
        try {
            System.out.println(String.format("postfix expr. %s evaluated as %.4f", "2 2.5 + 3 4 *",
                    postFixEval(parseExpr("2 2.5 + 3 4 *"))));
            assert false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }
        try {
            System.out.println(String.format("postfix expr. %s evaluated as %.4f", "2 5 * + ",
                    postFixEval(parseExpr("2 5 * + "))));
            assert false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }
    }

}
