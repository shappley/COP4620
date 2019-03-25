package COP4620.parser.semantics.nodes;

import COP4620.parser.Scope;

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
        return left.isValid(scope) && (right == null || right.isValid(scope));
    }
}
