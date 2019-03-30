package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;
import COP4620.parser.Symbol;

public class SimpleExpression extends Node {
    private AdditiveExpression left, right;
    private Relop relop;

    public SimpleExpression(AdditiveExpression expression) {
        this(expression, null, null);
    }

    public SimpleExpression(AdditiveExpression left, Relop relop, AdditiveExpression right) {
        this.left = left;
        this.relop = relop;
        this.right = right;
    }

    @Override
    public boolean isValid(Scope scope) {
        if (right != null) {
            if (left.evaluateType(scope) != right.evaluateType(scope)) {
                return false;
            } else if (left.evaluateType(scope) == Symbol.Type.FLOAT || right.evaluateType(scope) == Symbol.Type.FLOAT) {
                return false;
            }
        }
        return left.isValid(scope) && (right == null || right.isValid(scope));
    }

    @Override
    public Symbol.Type evaluateType(Scope scope) {
        return evaluateType(scope, left, right);
    }
}
