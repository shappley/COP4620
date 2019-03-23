package COP4620.parser.semantics.nodes;

import COP4620.parser.semantics.Node;

public class SimpleExpression extends Node {
    public SimpleExpression(AdditiveExpression expression) {
        this(expression, null, null);
    }

    public SimpleExpression(AdditiveExpression left, Relop relop, AdditiveExpression right) {

    }
}
